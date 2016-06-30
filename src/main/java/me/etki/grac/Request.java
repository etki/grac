package me.etki.grac;

import me.etki.grac.common.Metadata;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Request<T> {

    private String resource;
    private Action action;
    private T payload;
    private Metadata metadata;

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

    public Request setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public Request setAction(Action action) {
        this.action = action;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Request setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public T getPayload() {
        return payload;
    }

    public Request setPayload(T payload) {
        this.payload = payload;
        return this;
    }
}
