package me.etki.grac.application;

import com.google.common.base.Stopwatch;
import com.google.common.net.MediaType;
import me.etki.grac.concurrent.CompletableFutureFactory;
import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.io.DeserializationResult;
import me.etki.grac.io.SerializationResult;
import me.etki.grac.io.SynchronousSerializationManager;
import me.etki.grac.transport.Payload;
import me.etki.grac.transport.TransportManager;
import me.etki.grac.transport.TransportRequest;
import me.etki.grac.transport.TransportResponse;
import me.etki.grac.utility.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ApplicationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationClient.class);

    private final TransportManager transportManager;
    private final SynchronousSerializationManager serializationManager;
    private final CompletableFutureFactory completableFutureFactory;

    private final List<ApplicationLevelInterceptor> interceptors = new CopyOnWriteArrayList<>();

    public ApplicationClient(
            TransportManager transportManager,
            SynchronousSerializationManager serializationManager,
            CompletableFutureFactory completableFutureFactory) {

        this.transportManager = transportManager;
        this.serializationManager = serializationManager;
        this.completableFutureFactory = completableFutureFactory;
    }

    public ApplicationClient addInterceptor(ApplicationLevelInterceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public ApplicationClient removeInterceptor(ApplicationLevelInterceptor interceptor) {
        interceptors.remove(interceptor);
        return this;
    }

    public <I, O> CompletableFuture<ApplicationResponse<O>> execute(ApplicationRequest<I> request) {
        LOGGER.debug("Executing request {}", request);
        TypeSpec expectedType = request.getExpectedType();
        List<TypeSpec> fallbackTypes = request.getFallbackTypes();
        return preprocess(request)
                .thenCompose(this::convertRequest)
                .thenCompose(transportManager::execute)
                .thenCompose(response -> this.<O>convertResponse(response, expectedType, fallbackTypes))
                .thenCompose(this::postprocess)
                // ensuring that all callbacks will be run in correct thread pool
                .thenCompose(v -> completableFutureFactory.supply(() -> v));
    }

    private <T> CompletableFuture<ApplicationRequest<T>> preprocess(ApplicationRequest<T> request) {
        CompletableFuture<ApplicationRequest<T>> synchronizer = CompletableFutures.completed(request);
        if (interceptors.isEmpty()) {
            return synchronizer;
        }
        Stopwatch operationTimer = Stopwatch.createStarted();
        for (ApplicationLevelInterceptor interceptor : interceptors) {
            Stopwatch timer = Stopwatch.createStarted();
            synchronizer = synchronizer
                    .thenCompose(interceptor::processRequest)
                    .thenApply(value -> {
                        LOGGER.debug("Applied interceptor {} to request {} in {}", interceptor, request, timer);
                        return value;
                    });
        }
        return synchronizer
                .thenApply(value -> {
                    LOGGER.debug("Applied {} interceptors to response {} in {}", interceptors.size(), request,
                            operationTimer);
                    return value;
                });
    }

    private <T> CompletableFuture<ApplicationResponse<T>> postprocess(ApplicationResponse<T> response) {
        CompletableFuture<ApplicationResponse<T>> synchronizer = CompletableFutures.completed(response);
        if (interceptors.isEmpty()) {
            return synchronizer;
        }
        Stopwatch operationTimer = Stopwatch.createStarted();
        for (ApplicationLevelInterceptor interceptor : interceptors) {
            Stopwatch timer = Stopwatch.createStarted();
            synchronizer = synchronizer
                    .thenCompose(interceptor::processResponse)
                    .thenApply(value -> {
                        LOGGER.debug("Applied interceptor {} to response {} in {}", interceptor, response, timer);
                        return value;
                    });
        }
        return synchronizer
                .thenApply(value -> {
                    LOGGER.debug("Applied {} interceptors to response {} in {}", interceptors.size(), response,
                            operationTimer);
                    return value;
                });
    }

    private <T> CompletableFuture<TransportRequest> convertRequest(ApplicationRequest<T> request) {
        LOGGER.debug("Serializing request {}", request);
        return serialize(request.getPayload().orElse(null), request.getSerializationType())
                .thenApply(payload -> {
                    LOGGER.debug("Serialized request payload: {}", payload);
                    return assembleRequest(request, payload);
                });
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private <T> CompletableFuture<ApplicationResponse<T>> convertResponse(
            TransportResponse response,
            TypeSpec expectedType,
            List<TypeSpec> fallbackTypes) {

        return completableFutureFactory
                .call(() -> {
                    // tricking idea
                    if (response.getPayload().map(r -> r.getContent() == null).orElse(true)) {
                        LOGGER.debug("No payload has been provided for deserialization, short-circuiting with nulls");
                        return new DeserializationResult<T>(null, null);
                    }
                    LOGGER.debug("Deserializing payload {}", response.getPayload().get());
                    InputStream content = response.getPayload().get().getContent();
                    MediaType mimeType = response.getPayload().get().getMimeType();
                    return serializationManager.<T>deserialize(content, mimeType, expectedType, fallbackTypes);
                })
                .thenApply(result -> {
                    LOGGER.debug("Deserialized payload: {}", result);
                    return assembleResponse(response, result);
                });
    }

    private <T> CompletableFuture<Payload> serialize(T value, MediaType mimeType) {
        if (value == null) {
            LOGGER.debug("No payload has been found, short-circuiting");
            return CompletableFutures.completed(null);
        }
        LOGGER.debug("Serializing payload {}", value);
        try {
            SerializationResult result = serializationManager.serialize(value, mimeType);
            Payload payload = new Payload()
                    .setContent(result.getContent())
                    .setSize(result.getSize())
                    .setMimeType(mimeType);
            return CompletableFutures.completed(payload);
        } catch (Throwable e) {
            return CompletableFutures.exceptional(e);
        }
    }

    private <T> TransportRequest assembleRequest(ApplicationRequest<T> request, Payload payload) {
        return new TransportRequest()
                .setAction(request.getAction())
                .setResource(request.getResource())
                .setParameters(request.getParameters())
                .setMetadata(request.getMetadata())
                .setAcceptedMimeTypes(request.getAcceptedMimeTypes())
                .setAcceptedLocales(request.getAcceptedLocales())
                .setRetryPolicy(request.getRetryPolicy())
                .setTimeout(request.getTimeout())
                .setPayload(payload)
                .setClientIdentifier(request.getClientIdentifier().orElse(null));
    }

    private <T> ApplicationResponse<T> assembleResponse(
            TransportResponse response,
            DeserializationResult<T> result) {

        return new ApplicationResponse<T>()
                .setStatus(response.getStatus())
                .setDescription(response.getDescription().orElse(null))
                .setResult(result.getResult().orElse(null))
                .setAltResult(result.getAltResult().orElse(null))
                .setMetadata(response.getMetadata());
    }
}
