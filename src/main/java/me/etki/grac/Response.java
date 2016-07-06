package me.etki.grac;

import me.etki.grac.transport.ResponseStatus;
import me.etki.grac.transport.trace.Trace;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Response<T> {

    private ResponseStatus status;
    private String description;
    private T result;
    private Object altResult;
    private Map<String, List<Object>> metadata;
    private Trace trace;

    public ResponseStatus getStatus() {
        return status;
    }

    public Response<T> setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Response<T> setDescription(String description) {
        this.description = description;
        return this;
    }

    public Optional<T> getResult() {
        return Optional.ofNullable(result);
    }

    public Response<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public Optional<Object> getAltResult() {
        return Optional.ofNullable(altResult);
    }

    public Response<T> setAltResult(Object altResult) {
        this.altResult = altResult;
        return this;
    }

    public Map<String, List<Object>> getMetadata() {
        return metadata;
    }

    public Response<T> setMetadata(Map<String, List<Object>> metadata) {
        this.metadata = metadata;
        return this;
    }

    public Trace getTrace() {
        return trace;
    }

    public Response<T> setTrace(Trace trace) {
        this.trace = trace;
        return this;
    }
}
