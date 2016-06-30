package me.etki.grac.transport;

import com.google.common.base.Stopwatch;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.common.ResponseStatus;
import me.etki.grac.common.ServerDetails;
import me.etki.grac.concurrent.CompletableFutureFactory;
import me.etki.grac.concurrent.ScheduledHelper;
import me.etki.grac.exception.NoAvailableHostException;
import me.etki.grac.exception.NoSuitableTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultTransportManager implements TransportManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTransportManager.class);
    private static final CompletableFuture<Void> INSTANT_TIMEOUT = CompletableFuture.completedFuture(null);

    private final TransportRegistry transports;
    private final ServerRegistry servers;
    private final ScheduledHelper scheduler;
    private final List<TransportInterceptor> interceptors = new ArrayList<>();

    public DefaultTransportManager(TransportRegistry transports, ServerRegistry servers, ScheduledHelper scheduler) {

        this.transports = transports;
        this.servers = servers;
        this.scheduler = scheduler;
    }

    public DefaultTransportManager addInterceptor(TransportInterceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public DefaultTransportManager addInterceptors(Iterable<TransportInterceptor> interceptors) {
        interceptors.forEach(this.interceptors::add);
        return this;
    }

    public DefaultTransportManager removeInterceptor(TransportInterceptor interceptor) {
        interceptors.remove(interceptor);
        return this;
    }

    public DefaultTransportManager removeInterceptors(Iterable<TransportInterceptor> interceptor) {
        interceptors.forEach(this.interceptors::remove);
        return this;
    }

    public CompletableFuture<TLResponse> execute(TLRequest request) {
        LOGGER.debug("Executing request {}", request);
        Trace trace = new Trace()
                .start();
        RequestContext context = new RequestContext()
                .setTrace(trace)
                .setTlRequest(request);
        return executeInternal(request, context);
    }

    private CompletableFuture<TLResponse> executeInternal(TLRequest request, RequestContext context) {
        LOGGER.debug("Executing request {}, attempt {} of {}", request, context.getAttemptNumber(),
                request.getRetryPolicy().getMaximumAttempts());
        return getServer()
                .thenApply(server -> Converter.assembleTransportRequest(request, server))
                .thenCompose(this::preprocessRequest)
                .thenCompose(transportRequest -> {
                    context
                            .setTransportRequest(transportRequest)
                            .setStartedAt(Instant.now());
                    return executeInternal(transportRequest);
                })
                .thenApply(response -> {
                    context.setFinishedAt(Instant.now());
                    return response;
                })
                .thenCompose(this::postprocessResponse)
                .handle((response, throwable) -> processResponse(response, throwable, request, context))
                .thenCompose(f -> f)
                .thenApply(response -> {
                    context.getTrace().finish();
                    return response;
                });
    }

    private CompletableFuture<TransportResponse> executeInternal(TransportRequest request) {
        LOGGER.debug("Executing request {}", request);
        Optional<Transport> transport = transports.getTransport(request.getServerDetails().getProtocol());
        if (!transport.isPresent()) {
            return CompletableFutureFactory.completedExceptionally(new NoSuitableTransportException());
        }
        CompletableFuture<TransportResponse> execution = transport.get().execute(request);
        LOGGER.debug("Setting timeout of {} ms to request {} execution", request.getTimeout(), request);
        CompletableFuture<Void> timeout = scheduler
                .setTimeout(execution, request.getTimeout(), TimeUnit.MILLISECONDS)
                .thenRun(() ->
                        LOGGER.debug("Terminating request {} due to timeout of {} ms", request, request.getTimeout())
                );
        execution.handle((result, throwable) -> timeout.cancel(true));
        return execution;
    }

    private CompletableFuture<TLResponse> processResponse(TransportResponse response, Throwable throwable,
                                                          TLRequest request, RequestContext context) {

        RetryPolicy retryPolicy = request.getRetryPolicy();
        int attemptNumber = context.getAttemptNumber();
        int nextAttemptNumber = attemptNumber + 1;
        boolean shouldPerformNextAttempt = retryPolicy.shouldPerformAttempt(nextAttemptNumber);
        Interaction interaction = new Interaction()
                .setRequest(context.getTransportRequest())
                .setResponse(response)
                .setThrowable(throwable)
                .setStartedAt(context.getStartedAt())
                .setFinishedAt(context.getFinishedAt());
        context.getTrace().addInteraction(interaction);
        if (response != null) {
            ResponseStatus status = response.getStatus();
            if (ResponseStatus.OK.equals(status)) {
                LOGGER.debug("Received OK response, returning");
                return CompletableFuture.completedFuture(Converter.assembleTLResponse(response, context));
            }
            if (ResponseStatus.CLIENT_ERROR.equals(status) &&
                    (!retryPolicy.shouldRetryOnClientError() || !shouldPerformNextAttempt)) {
                LOGGER.debug("Received client error response, but can't retry - returning");
                return CompletableFuture.completedFuture(Converter.assembleTLResponse(response, context));
            }
            if (ResponseStatus.SERVER_ERROR.equals(status) &&
                    (!retryPolicy.shouldRetryOnServerError() || !shouldPerformNextAttempt)) {
                LOGGER.debug("Received server error response, but can't retry - returning");
                return CompletableFuture.completedFuture(Converter.assembleTLResponse(response, context));
            }
        }
        if (!shouldPerformNextAttempt) {
            LOGGER.debug("Terminated with exception {} ({}) and may not retry, completing exceptionally",
                    throwable.getClass(), throwable.getMessage());
            return CompletableFutureFactory.completedExceptionally(throwable);
        }
        RequestContext nextRequestContext = new RequestContext()
                .setTlRequest(context.getTlRequest())
                .setTrace(context.getTrace())
                .setAttemptNumber(nextAttemptNumber);
        long delay = retryPolicy.calculateDelay(nextAttemptNumber);
        CompletableFuture<Void> timeout;
        if (delay > 0) {
            LOGGER.debug("Retry policy ordered to proceed with a cooldown of {} ms, inserting delay", delay);
            timeout = scheduler.await(delay, TimeUnit.MILLISECONDS);
        } else {
            LOGGER.debug("Retry policy ordered to proceed with no cooldown, short-circuiting");
            timeout = INSTANT_TIMEOUT;
        }
        return timeout.thenCompose(v -> executeInternal(request, nextRequestContext));
    }

    private CompletableFuture<ServerDetails> getServer() {
        return servers
                .getServer()
                .thenCompose(server -> {
                    if (!server.isPresent()) {
                        String message = "Server manager could not return next server details";
                        return CompletableFutureFactory.completedExceptionally(new NoAvailableHostException(message));
                    }
                    return CompletableFuture.completedFuture(server.get());
                });
    }

    private CompletableFuture<TransportRequest> preprocessRequest(TransportRequest request) {
        CompletableFuture<TransportRequest> promise = CompletableFuture.completedFuture(request);
        for (TransportInterceptor interceptor : interceptors) {
            promise = promise.thenCompose(intermediate -> {
                Stopwatch timer = Stopwatch.createStarted();
                return interceptor
                        .processRequest(intermediate)
                        .thenApply(result -> {
                            LOGGER.debug("Processed request {} with interceptor {} in {}", intermediate, interceptor,
                                    timer);
                            return result;
                        });
            });
        }
        return promise;
    }

    private CompletableFuture<TransportResponse> postprocessResponse(TransportResponse response) {
        CompletableFuture<TransportResponse> promise = CompletableFuture.completedFuture(response);
        for (TransportInterceptor interceptor : interceptors) {
            promise = promise.thenCompose(intermediate -> {
                Stopwatch timer = Stopwatch.createStarted();
                return interceptor
                        .processResponse(intermediate)
                        .thenApply(result -> {
                            LOGGER.debug("Processed response {} with interceptor {} in {}", intermediate, interceptor,
                                    timer);
                            return result;
                        });
            });
        }
        return promise;
    }
}
