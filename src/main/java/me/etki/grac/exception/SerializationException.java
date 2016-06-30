package me.etki.grac.exception;

/**
 * Designed to be thrown whenever (de)serialization fails under normal conditions, i.e. current deserializer can't
 * deserialize desired type from a perfectly valid stream.
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class SerializationException extends Exception {

    public SerializationException() {
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

    public SerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
