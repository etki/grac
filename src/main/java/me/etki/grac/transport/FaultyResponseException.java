package me.etki.grac.transport;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class FaultyResponseException extends Exception {

    public FaultyResponseException() {
    }

    public FaultyResponseException(String message) {
        super(message);
    }

    public FaultyResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaultyResponseException(Throwable cause) {
        super(cause);
    }

    public FaultyResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
