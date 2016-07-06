package me.etki.grac.transport;

import me.etki.grac.transport.trace.Trace;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportResponse {

    private ResponseStatus status;
    private String description;
    private Map<String, List<Object>> metadata;
    private Payload payload;
    private Trace trace;

    public ResponseStatus getStatus() {
        return status;
    }

    public TransportResponse setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public Map<String, List<Object>> getMetadata() {
        return metadata;
    }

    public TransportResponse setMetadata(Map<String, List<Object>> metadata) {
        this.metadata = metadata;
        return this;
    }

    public Optional<Payload> getPayload() {
        return Optional.ofNullable(payload);
    }

    public TransportResponse setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public TransportResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public Trace getTrace() {
        return trace;
    }

    public TransportResponse setTrace(Trace trace) {
        this.trace = trace;
        return this;
    }
}
