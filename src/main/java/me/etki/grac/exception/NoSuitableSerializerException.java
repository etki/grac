package me.etki.grac.exception;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class NoSuitableSerializerException extends SerializationException {

    public NoSuitableSerializerException() {
    }

    public NoSuitableSerializerException(String message) {
        super(message);
    }

    public NoSuitableSerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuitableSerializerException(Throwable cause) {
        super(cause);
    }

    public NoSuitableSerializerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
