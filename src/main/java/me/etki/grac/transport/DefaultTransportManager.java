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

import java.io.ByteArrayInputStream;
import java.time.Instant;
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

    public DefaultTransportManager(
            ServerRegistry servers,
            TransportRequestExecutor requestExecutor,
            DelayService delayService) {

        this.servers = servers;
        this.requestExecutor = requestExecutor;
        this.delayService = delayService;
    }

    public CompletableFuture<TransportResponse> execute(TransportRequest request) {
        request.validate();
        return executeInternal(request, new Trace().start(), 1);
    }

    private CompletableFuture<TransportResponse> executeInternal(TransportRequest request, Trace trace, int attempt) {
        LOGGER.debug("Executing request {}, attempt #{}", request, attempt);
        RetryPolicy retryPolicy = request.getRetryPolicy();
        return getServer()
                .thenApply(server -> assembleServerRequest(request, server))
                .thenCompose(serverRequest -> executeAndRecord(serverRequest, trace))
                // todo refactoring candidate!
                .handle((response, throwable) -> {
                    LOGGER.debug("Post-processing request result (attempt: {}, response: {}, throwable: {})", attempt,
                            response, throwable == null ? null : throwable.getClass());
                    boolean shouldRetry = retryPolicy.shouldRetry(response, throwable, attempt);
                    // todo dirty code
                    boolean mayRetry = request.getPayload()
                            .map(p -> p.getContent() instanceof ByteArrayInputStream)
                            .orElse(false);
                    if (shouldRetry && request.getAction().isIdempotent() && mayRetry) {
                        if (request.getPayload() != null) {
                            LOGGER.debug("Resetting payload input stream");
                            //noinspection OptionalGetWithoutIsPresent
                            ((ByteArrayInputStream) request.getPayload().get().getContent()).reset();
                        }
                        long delay = retryPolicy.calculateDelay(attempt + 1);
                        LOGGER.debug("Retry policy ordered to perform another attempt with delay of {} ms, retrying",
                                delay);
                        return delayService
                                .await(retryPolicy.calculateDelay(attempt + 1), TimeUnit.MILLISECONDS)
                                .thenCompose(v -> executeInternal(request, trace, attempt + 1));
                    } else if (!request.getAction().isIdempotent()) {
                        LOGGER.debug("Retry policy allowed to retry, but request action is not idempotent, returning " +
                                "results");
                    } else if (!mayRetry) {
                        LOGGER.debug("Retry policy allowed to retry, but provided payload contains input stream that " +
                                "may not be reset; returning results early");
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
