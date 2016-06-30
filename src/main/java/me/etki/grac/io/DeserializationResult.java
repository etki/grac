package me.etki.grac.io;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DeserializationResult<T> {

    private final T result;
    private final Object altResult;

    public DeserializationResult(T result, Object altResult) {
        this.result = result;
        this.altResult = altResult;
    }

    public T getResult() {
        return result;
    }

    public Object getAltResult() {
        return altResult;
    }

    public static <T> DeserializationResult<T> normal(T result) {
        return new DeserializationResult<>(result, null);
    }

    public static <T> DeserializationResult<T> alternative(Object result) {
        return new DeserializationResult<>(null, result);
    }
}
