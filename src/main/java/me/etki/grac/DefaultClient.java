package me.etki.grac;

import com.google.common.net.MediaType;
import me.etki.grac.common.Payload;
import me.etki.grac.io.SynchronousSerializationManager;
import me.etki.grac.transport.TLRequest;
import me.etki.grac.transport.TLResponse;
import me.etki.grac.transport.TransportManager;
import me.etki.grac.utility.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultClient implements Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClient.class);

    private final TransportManager transportManager;
    private final SynchronousSerializationManager serializationManager;
    private final RequestResponseProcessor processor;
    private final RequestOptions defaultRequestOptions;
    private final ClientOptions clientOptions;

    public DefaultClient(TransportManager transportManager, SynchronousSerializationManager serializationManager,
                         RequestResponseProcessor processor, RequestOptions defaultRequestOptions,
                         ClientOptions clientOptions) {

        this.transportManager = transportManager;
        this.serializationManager = serializationManager;
        this.processor = processor;
        this.defaultRequestOptions = defaultRequestOptions;
        this.clientOptions = clientOptions;
    }

    @Override
    public RequestOptions getDefaultRequestOptions() {
        return defaultRequestOptions;
    }

    @Override
    public <I, O> CompletableFuture<Response<O>> execute(Request<I> request, TypeSpec expectedType,
                                                         RequestOptions options) {

        RequestOptions requestOptions = RequestOptions.merge(options.copy(), defaultRequestOptions);
        AssembledRequest<I> assembledRequest = assembleRequest(request, expectedType, requestOptions);
        return processor.processRequest(assembledRequest)
                .thenCompose(this::<I, O>execute)
                .thenCompose(processor::processResponse)
                .thenApply(this::extractResponse);
    }

    private <I> AssembledRequest<I> assembleRequest(Request<I> request, TypeSpec expectedType, RequestOptions options) {
        MediaType serializationType = Optional
                .of(options.getSerializationType())
                .orElseGet(this.clientOptions::getDefaultSerializationType);
        return new AssembledRequest<I>()
                .setResource(request.getResource())
                .setAction(request.getAction())
                .setPayload(request.getPayload())
                .setMetadata(request.getMetadata())
                .setExpectedType(expectedType)
                .setRetryPolicy(options.getRetryPolicy())
                .setSerializationType(serializationType)
                .setAcceptedTypes(options.getAcceptedTypes())
                .setAcceptedLocales(options.getAcceptedLocales())
                .setFallbackTypes(options.getFallbackTypes())
                .setTimeout(options.getTimeout());
    }

    private <I> CompletableFuture<TLRequest> convertRequest(AssembledRequest<I> request) {
        CompletableFuture<Payload> payloadFuture = CompletableFuture.completedFuture(null); // todo
        TLRequest tlRequest = new TLRequest()
                .setResource(request.getResource())
                .setAction(request.getAction())
                .setAcceptedTypes(request.getAcceptedTypes())
                .setAcceptedLocales(request.getAcceptedLocales())
                .setTimeout(request.getTimeout())
                .setMetadata(request.getMetadata());
        return payloadFuture.thenApply(tlRequest::setPayload);
    }

    private <I, O> CompletableFuture<AssembledResponse<I, O>> assembleResponse(AssembledRequest<I> request, TLRequest tlRequest, TLResponse tlResponse) {
        CompletableFuture<O> output = CompletableFuture.completedFuture(null); // todo
        AssembledResponse<I, O> response = new AssembledResponse<I, O>()
                .setStatus(tlResponse.getStatus())
                .setDescription(tlResponse.getDescription())
                .setMetadata(tlResponse.getMetadata())
                .setRequest(request)
                .setTransportLayerRequest(tlRequest)
                .setTransportLayerResponse(tlResponse);
        return output
                .thenApply(response::setResult);
    }

    private <I, O> Response<O> extractResponse(AssembledResponse<I, O> response) {
        return new Response<O>()
                .setStatus(response.getStatus())
                .setDescription(response.getDescription())
                .setResult(response.getResult())
                .setAltResult(response.getAltResult())
                .setMetadata(response.getMetadata())
                .setTransportRequest(response.getTransportLayerRequest())
                .setTransportResponse(response.getTransportLayerResponse());
    }

    private <I, O> CompletableFuture<AssembledResponse<I, O>> execute(AssembledRequest<I> request) {
        // todo ugh
        return convertRequest(request)
                .thenCompose(tlRequest ->
                        transportManager
                                .execute(tlRequest)
                                .thenCompose(tlResponse -> assembleResponse(request, tlRequest, tlResponse))
                );
    }
}
