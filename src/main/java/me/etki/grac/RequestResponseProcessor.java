package me.etki.grac;

import me.etki.grac.common.ResponseStatus;
import me.etki.grac.exception.ClientErrorException;
import me.etki.grac.exception.InvalidResponseFormatException;
import me.etki.grac.exception.ServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class RequestResponseProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseProcessor.class);

    private final List<Interceptor> interceptors;
    private final ResponseProcessingOptions responseProcessingOptions;

    public RequestResponseProcessor(List<Interceptor> interceptors, ResponseProcessingOptions responseProcessingOptions) {
        this.interceptors = interceptors;
        this.responseProcessingOptions = responseProcessingOptions;
    }

    public <I> CompletableFuture<AssembledRequest<I>> processRequest(AssembledRequest<I> request) {
        LOGGER.debug("Applying interceptors to request {}", request);
        CompletableFuture<AssembledRequest<I>> future = CompletableFuture.completedFuture(request);
        for (Interceptor interceptor : interceptors) {
            future = future.thenCompose(result -> {
                LOGGER.debug("Applying interceptor {} to request {}", interceptor, request);
                return interceptor.processRequest(request);
            });
        }
        return future;
    }

    public <I, O> CompletableFuture<AssembledResponse<I, O>> processResponse(AssembledResponse<I, O> response) {

        return applyInterceptors(response).thenCompose(this::postProcessResponse);
    }

    private <I, O> CompletableFuture<AssembledResponse<I, O>> applyInterceptors(AssembledResponse<I, O> response) {

        LOGGER.debug("Applying interceptors to response {}", response);
        CompletableFuture<AssembledResponse<I, O>> future = CompletableFuture.completedFuture(response);
        for (Interceptor interceptor : interceptors) {
            future = future.thenCompose(result -> {
                LOGGER.debug("Applying interceptor {} to response {}", interceptor, result);
                return interceptor.processResponse(response);
            });
        }
        return future;
    }

    private <I, O> CompletableFuture<AssembledResponse<I, O>> postProcessResponse(AssembledResponse<I, O> response) {
        if (ResponseStatus.CLIENT_ERROR.equals(response.getStatus()) && responseProcessingOptions.getThrowOnClientError()) {
            CompletableFuture<AssembledResponse<I, O>> future = new CompletableFuture<>();
            String message = "Response resulted in client error: " + response.getDescription();
            ClientErrorException exception = new ClientErrorException(message);
            exception.setResponse(response);
            future.completeExceptionally(exception);
            return future;
        }
        if (ResponseStatus.SERVER_ERROR.equals(response.getStatus()) && responseProcessingOptions.getThrowOnServerError()) {
            CompletableFuture<AssembledResponse<I, O>> future = new CompletableFuture<>();
            String message = "Response resulted in server error: " + response.getDescription();
            ServerErrorException exception = new ServerErrorException(message);
            exception.setResponse(response);
            future.completeExceptionally(exception);
            return future;
        }
        if (response.getAltResult() != null && responseProcessingOptions.getThrowOnInvalidResponsePayloadType()) {
            CompletableFuture<AssembledResponse<I, O>> future = new CompletableFuture<>();
            String message = "Server returned unexpected structure";
            InvalidResponseFormatException exception = new InvalidResponseFormatException(message);
            exception.setResponse(response);
            future.completeExceptionally(exception);
            return future;
        }
        return CompletableFuture.completedFuture(response);
    }
}
