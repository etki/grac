package me.etki.grac.transport;

import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.concurrent.DelayService;
import me.etki.grac.exception.NoAvailableHostException;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.transport.server.Server;
import me.etki.grac.transport.server.ServerRegistry;
import me.etki.grac.transport.trace.Interaction;
import me.etki.grac.transport.trace.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
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

    private final ServerRegistry servers;
    private final TransportRequestExecutor requestExecutor;
    private final DelayService delayService;
    private final int inputStreamMarkLimit;

    public DefaultTransportManager(
            ServerRegistry servers,
            TransportRequestExecutor requestExecutor,
            DelayService delayService,
            int inputStreamMarkLimit) {

        this.servers = servers;
        this.requestExecutor = requestExecutor;
        this.delayService = delayService;
        this.inputStreamMarkLimit = inputStreamMarkLimit;
    }

    public CompletableFuture<TransportResponse> execute(TransportRequest request) {
        request.validate();
        return executeInternal(request, new Trace().start(), 1);
    }

    private CompletableFuture<TransportResponse> executeInternal(TransportRequest request, Trace trace, int attempt) {
        LOGGER.debug("Executing request {}, attempt #{}", request, attempt);
        RetryPolicy retryPolicy = request.getRetryPolicy();
        return getServer()
                // todo dirty code
                .thenApply(server -> {
                    ServerRequest serverRequest = assembleServerRequest(request, server);
                    Optional<InputStream> content = serverRequest.getPayload().map(Payload::getContent);
                    if (content.isPresent()) {
                        if (content.get().markSupported()) {
                            LOGGER.debug("Found mark/reset supporting payload {}, reserving {} bytes for rollback",
                                    content.get(), inputStreamMarkLimit);
                            content.get().mark(inputStreamMarkLimit);
                        } else {
                            LOGGER.debug("Provided request payload doesn't support mark/reset, retry " +
                                    "won't be possible");
                        }
                    }
                    return serverRequest;
                })
                .thenCompose(serverRequest -> executeAndRecord(serverRequest, trace))
                // todo refactoring candidate!
                .handle((response, throwable) -> {
                    LOGGER.debug("Post-processing request result (attempt: {}, response: {}, throwable: {})", attempt,
                            response, throwable == null ? null : throwable.getClass());
                    Optional<InputStream> payloadStream = request.getPayload().map(Payload::getContent);
                    boolean shouldRetry = retryPolicy.shouldRetry(response, throwable, attempt);
                    // todo dirty code
                    if (shouldRetry && request.getAction().isIdempotent()) {
                        boolean mayRetry = true;
                        if (payloadStream.isPresent()) {
                            LOGGER.debug("Request contains payload, trying to reset it");
                            try {
                                payloadStream.get().reset();
                                payloadStream.get().mark(inputStreamMarkLimit);
                            } catch (IOException e) {
                                mayRetry = false;
                            }
                        }
                        if (mayRetry) {
                            long delay = retryPolicy.calculateDelay(attempt + 1);
                            LOGGER.debug("Retry policy ordered to perform another attempt with delay of {} ms, " +
                                    "retrying", delay);
                            return delayService
                                    .await(retryPolicy.calculateDelay(attempt + 1), TimeUnit.MILLISECONDS)
                                    .thenCompose(v -> executeInternal(request, trace, attempt + 1));
                        } else {
                            LOGGER.debug("Retry policy allowed to retry, but provided payload contains input stream " +
                                    "that may not be reset; returning results early");
                        }
                    } else if (!request.getAction().isIdempotent()) {
                        LOGGER.debug("Retry policy allowed to retry, but request action is not idempotent, returning " +
                                "results");
                    } else {
                        LOGGER.debug("Retry policy ordered to proceed with current results");
                    }
                    return CompletableFutures
                            .oneOf(response, throwable)
                            .thenApply(r -> assembleTransportResponse(r, trace.finish()));
                })
                .thenCompose(f -> f);
    }

    // todo i'm not sure that should be combined
    private CompletableFuture<ServerResponse> executeAndRecord(ServerRequest request, Trace trace) {
        Instant startedAt = Instant.now();
        return requestExecutor
                .execute(request)
                .handle((response, throwable) -> {
                    LOGGER.debug("Recording request {} result: response {}, throwable {}", request, response,
                            throwable == null ? null : throwable.getClass());
                    trace.addInteraction(new Interaction(request, response, throwable, startedAt, Instant.now()));
                    return CompletableFutures.oneOf(response, throwable);
                })
                .thenCompose(f -> f);
    }

    private CompletableFuture<Server> getServer() {
        return servers
                .getServer()
                .thenCompose(server -> {
                    if (!server.isPresent()) {
                        String message = "Server manager could not return next server details";
                        return CompletableFutures.exceptional(new NoAvailableHostException(message));
                    }
                    return CompletableFuture.completedFuture(server.get());
                });
    }

    private static TransportResponse assembleTransportResponse(ServerResponse response, Trace trace) {
        return new TransportResponse()
                .setStatus(response.getStatus())
                .setDescription(response.getDescription().orElse(null))
                .setMetadata(response.getMetadata())
                .setPayload(response.getPayload().orElse(null))
                .setTrace(trace);
    }

    private static ServerRequest assembleServerRequest(TransportRequest request, Server details) {
        return new ServerRequest()
                .setServer(details)
                .setAction(request.getAction())
                .setResource(request.getResource())
                .setParameters(request.getParameters())
                .setAcceptedMimeTypes(request.getAcceptedMimeTypes())
                .setAcceptedLocales(request.getAcceptedLocales())
                .setClientIdentifier(request.getClientIdentifier().orElse(null))
                .setTimeout(request.getTimeout())
                .setMetadata(request.getMetadata())
                .setPayload(request.getPayload().orElse(null));
    }
}
