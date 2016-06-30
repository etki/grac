package me.etki.grac.exception;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class NoAvailableHostException extends Exception {

    public NoAvailableHostException() {
    }

    public NoAvailableHostException(String message) {
        super(message);
    }

    public NoAvailableHostException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAvailableHostException(Throwable cause) {
        super(cause);
    }

    public NoAvailableHostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
