package me.etki.grac;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ResponseProcessingOptions {
    private boolean throwOnClientError = true;
    private boolean throwOnServerError = true;
    private boolean throwOnInvalidResponsePayloadType = true;

    public boolean getThrowOnClientError() {
        return throwOnClientError;
    }

    public ResponseProcessingOptions setThrowOnClientError(boolean throwOnClientError) {
        this.throwOnClientError = throwOnClientError;
        return this;
    }

    public boolean getThrowOnServerError() {
        return throwOnServerError;
    }

    public ResponseProcessingOptions setThrowOnServerError(boolean throwOnServerError) {
        this.throwOnServerError = throwOnServerError;
        return this;
    }

    public boolean getThrowOnInvalidResponsePayloadType() {
        return throwOnInvalidResponsePayloadType;
    }

    public ResponseProcessingOptions setThrowOnInvalidResponsePayloadType(boolean throwOnInvalidResponsePayloadType) {
        this.throwOnInvalidResponsePayloadType = throwOnInvalidResponsePayloadType;
        return this;
    }
}
