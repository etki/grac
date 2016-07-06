package me.etki.grac.io;

import java.util.Optional;

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

    public Optional<T> getResult() {
        return Optional.ofNullable(result);
    }

    public Optional<Object> getAltResult() {
        return Optional.ofNullable(altResult);
    }

    public static <T> DeserializationResult<T> normal(T result) {
        return new DeserializationResult<>(result, null);
    }

    public static <T> DeserializationResult<T> alternative(Object result) {
        return new DeserializationResult<>(null, result);
    }
}
