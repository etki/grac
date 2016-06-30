package me.etki.grac.exception;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class NoSuitableTransportException extends Exception {

    public NoSuitableTransportException() {
        this(null, null);
    }

    public NoSuitableTransportException(String message) {
        this(message, null);
    }

    public NoSuitableTransportException(String message, Throwable cause) {
        this(message, cause, false, false);
    }

    public NoSuitableTransportException(Throwable cause) {
        this(null, cause);
    }

    public NoSuitableTransportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
