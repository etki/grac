package me.etki.grac.transport;

import me.etki.grac.common.Metadata;
import me.etki.grac.common.Payload;
import me.etki.grac.common.ResponseStatus;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportResponse {

    private ResponseStatus status;
    private String description;
    private Metadata metadata;
    private Payload payload;

    public ResponseStatus getStatus() {
        return status;
    }

    public TransportResponse setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TransportResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public TransportResponse setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Payload getPayload() {
        return payload;
    }

    public TransportResponse setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }
}
