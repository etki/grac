package me.etki.grac.exception;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ClientErrorException extends AbstractIncorrectResponseException {

    public ClientErrorException() {
    }

    public ClientErrorException(String message) {
        super(message);
    }

    public ClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientErrorException(Throwable cause) {
        super(cause);
    }

    public ClientErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
