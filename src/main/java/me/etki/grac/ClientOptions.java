package me.etki.grac;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ClientOptions {

    private boolean throwOnClientError = true;
    private boolean throwOnServerError = true;
    private boolean throwOnInvalidResponsePayloadType = true;

    public boolean shouldThrowOnClientError() {
        return throwOnClientError;
    }

    public ClientOptions setThrowOnClientError(boolean throwOnClientError) {
        this.throwOnClientError = throwOnClientError;
        return this;
    }

    public boolean shouldThrowOnServerError() {
        return throwOnServerError;
    }

    public ClientOptions setThrowOnServerError(boolean throwOnServerError) {
        this.throwOnServerError = throwOnServerError;
        return this;
    }

    public boolean shouldThrowOnInvalidResponsePayloadType() {
        return throwOnInvalidResponsePayloadType;
    }

    public ClientOptions setThrowOnInvalidResponsePayloadType(boolean throwOnInvalidResponsePayloadType) {
        this.throwOnInvalidResponsePayloadType = throwOnInvalidResponsePayloadType;
        return this;
    }
}
