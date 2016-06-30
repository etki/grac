package me.etki.grac.exception;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class InvalidResponseFormatException extends AbstractIncorrectResponseException {

    public InvalidResponseFormatException() {
    }

    public InvalidResponseFormatException(String message) {
        super(message);
    }

    public InvalidResponseFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidResponseFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidResponseFormatException(String message, Throwable cause, boolean enableSuppression,
                                          boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
