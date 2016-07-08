package me.etki.grac;

import me.etki.grac.application.ApplicationClient;
import me.etki.grac.application.ApplicationRequest;
import me.etki.grac.application.ApplicationResponse;
import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.exception.ClientErrorException;
import me.etki.grac.exception.InvalidResponseFormatException;
import me.etki.grac.exception.ServerErrorException;
import me.etki.grac.transport.ResponseStatus;
import me.etki.grac.utility.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultClient implements Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClient.class);

    private final ApplicationClient client;
    private final RequestOptions defaultRequestOptions;
    private final ClientOptions clientOptions;

    public DefaultClient(ApplicationClient client, RequestOptions defaultRequestOptions,
                         ClientOptions clientOptions) {

        this.client = client;
        this.defaultRequestOptions = defaultRequestOptions;
        this.clientOptions = clientOptions;
    }

    @Override
    public RequestOptions getDefaultRequestOptions() {
        return defaultRequestOptions;
    }

    public <I, O> CompletableFuture<Response<O>> execute(Request<I> request, TypeSpec expectedType,
                                                         RequestOptions options) {

        RequestOptions requestOptions = RequestOptions.merge(options, defaultRequestOptions);
        ApplicationRequest<I> applicationRequest = assembleRequest(request, expectedType, requestOptions);
        return client
                .<I, O>execute(applicationRequest)
                .thenCompose(this::postprocess)
                .thenApply(this::assembleResponse);
    }

    private <O> CompletableFuture<ApplicationResponse<O>> postprocess(ApplicationResponse<O> response) {
        if (ResponseStatus.CLIENT_ERROR.equals(response.getStatus()) && clientOptions.shouldThrowOnClientError()) {
            String message = "Response resulted in client error: " + response.getDescription();
            ClientErrorException exception = new ClientErrorException(message);
            exception.setResponse(response);
            return CompletableFutures.exceptional(exception);
        }
        if (ResponseStatus.SERVER_ERROR.equals(response.getStatus()) && clientOptions.shouldThrowOnServerError()) {
            String message = "Response resulted in server error: " + response.getDescription();
            ServerErrorException exception = new ServerErrorException(message);
            exception.setResponse(response);
            return CompletableFutures.exceptional(exception);
        }
        if (response.getAltResult().isPresent() && clientOptions.shouldThrowOnInvalidResponsePayloadType()) {
            String message = "Server returned unexpected structure";
            InvalidResponseFormatException exception = new InvalidResponseFormatException(message);
            exception.setResponse(response);
            return CompletableFutures.exceptional(exception);
        }
        return CompletableFutures.completed(response);
    }

    private <I> ApplicationRequest<I> assembleRequest(
            Request<I> request,
            TypeSpec expectedType,
            RequestOptions options) {

        return new ApplicationRequest<I>()
                .setAction(request.getAction())
                .setResource(request.getResource())
                .setParameters(Optional.ofNullable(request.getParameters()).orElseGet(HashMap::new))
                .setPayload(request.getPayload().orElse(null))
                .setTimeout(options.getTimeout())
                .setExpectedType(expectedType)
                .setFallbackTypes(Optional.ofNullable(options.getFallbackObjectTypes()).orElseGet(ArrayList::new))
                .setRetryPolicy(options.getRetryPolicy())
                .setSerializationType(options.getSerializationType())
                .setAcceptedLocales(Optional.ofNullable(options.getAcceptedLocales()).orElseGet(ArrayList::new))
                .setAcceptedMimeTypes(Optional.ofNullable(options.getAcceptedMimeTypes()).orElseGet(ArrayList::new))
                .setMetadata(Optional.ofNullable(request.getMetadata()).orElseGet(HashMap::new))
                .setClientIdentifier(options.getClientIdentifier());
    }

    private <O> Response<O> assembleResponse(ApplicationResponse<O> response) {
        return new Response<O>()
                .setStatus(response.getStatus())
                .setDescription(response.getDescription().orElse(null))
                .setResult(response.getResult().orElse(null))
                .setAltResult(response.getAltResult().orElse(null))
                .setMetadata(response.getMetadata())
                .setTrace(response.getTrace());
    }
}
