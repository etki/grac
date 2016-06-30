package me.etki.grac.transport;

import me.etki.grac.common.Metadata;
import me.etki.grac.common.Payload;
import me.etki.grac.common.ResponseStatus;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TLResponse {

    private ResponseStatus status;
    private Metadata metadata;
    private Payload payload;
    private String description;
    private Trace trace;

    public ResponseStatus getStatus() {
        return status;
    }

    public TLResponse setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public TLResponse setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Payload getPayload() {
        return payload;
    }

    public TLResponse setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TLResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public Trace getTrace() {
        return trace;
    }

    public TLResponse setTrace(Trace trace) {
        this.trace = trace;
        return this;
    }
}
