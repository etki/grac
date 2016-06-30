package me.etki.grac.transport;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportRequestResult {

    private TransportResponse response;
    private Throwable exception;

    public TransportResponse getResponse() {
        return response;
    }

    public TransportRequestResult setResponse(TransportResponse response) {
        this.response = response;
        return this;
    }

    public Throwable getException() {
        return exception;
    }

    public TransportRequestResult setException(Throwable exception) {
        this.exception = exception;
        return this;
    }
}
