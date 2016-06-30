package me.etki.grac.exception;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ProtocolMismatchException extends Exception {

    public ProtocolMismatchException() {
    }

    public ProtocolMismatchException(String message) {
        super(message);
    }

    public ProtocolMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolMismatchException(Throwable cause) {
        super(cause);
    }

    public ProtocolMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
