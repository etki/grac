package me.etki.grac;

import me.etki.grac.common.Action;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Request<T> {

    private String resource;
    private Action action;
    private Map<String, List<Object>> parameters;
    private Map<String, List<Object>> metadata;
    private T payload;

    public Request() {
    }

    public Request(String resource, Action action) {
        this.resource = resource;
        this.action = action;
    }

    public Request(String resource, Action action, T payload) {
        this.resource = resource;
        this.action = action;
        this.payload = payload;
    }

    public String getResource() {
        return resource;
    }

    public Request<T> setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public Request<T> setAction(Action action) {
        this.action = action;
        return this;
    }

    public Map<String, List<Object>> getParameters() {
        return parameters;
    }

    public Request<T> setParameters(Map<String, List<Object>> parameters) {
        this.parameters = parameters;
        return this;
    }

    public Map<String, List<Object>> getMetadata() {
        return metadata;
    }

    public Request<T> setMetadata(Map<String, List<Object>> metadata) {
        this.metadata = metadata;
        return this;
    }

    public Optional<T> getPayload() {
        return Optional.ofNullable(payload);
    }

    public Request<T> setPayload(T payload) {
        this.payload = payload;
        return this;
    }
}
