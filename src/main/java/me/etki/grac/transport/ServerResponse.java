package me.etki.grac.transport;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ServerResponse {

    private ResponseStatus status;
    private String description;
    private Map<String, List<Object>> metadata;
    private Payload payload;

    public ResponseStatus getStatus() {
        return status;
    }

    public ServerResponse setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public ServerResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, List<Object>> getMetadata() {
        return metadata;
    }

    public ServerResponse setMetadata(Map<String, List<Object>> metadata) {
        this.metadata = metadata;
        return this;
    }

    public Optional<Payload> getPayload() {
        return Optional.ofNullable(payload);
    }

    public ServerResponse setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public String toString() {
        return "ServerResponse {status=" + status + ", description='" + description + "', " +
                "metadata=" + metadata + ", payload={" + payload + "}";
    }
}
