package me.etki.grac.exception;

import me.etki.grac.AssembledResponse;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public abstract class AbstractIncorrectResponseException extends Exception {

    private AssembledResponse response;

    public AssembledResponse getResponse() {
        return response;
    }

    public AbstractIncorrectResponseException setResponse(AssembledResponse response) {
        this.response = response;
        return this;
    }

    public AbstractIncorrectResponseException() {
    }

    public AbstractIncorrectResponseException(String message) {
        super(message);
    }

    public AbstractIncorrectResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractIncorrectResponseException(Throwable cause) {
        super(cause);
    }

    public AbstractIncorrectResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
