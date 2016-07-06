package me.etki.grac.infrastructure;

import me.etki.grac.transport.Payload;
import me.etki.grac.transport.ResponseStatus;
import me.etki.grac.transport.ServerResponse;
import me.etki.grac.transport.TransportResponse;
import me.etki.grac.transport.trace.Trace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Responses {

    public static ServerResponse server() {
        return new ServerResponse();
    }

    public static ServerResponse server(ResponseStatus status) {
        return server()
                .setStatus(status);
    }

    public static ServerResponse server(ResponseStatus status, Payload payload) {
        return server(status)
                .setPayload(payload);
    }

    public static ServerResponse server(ResponseStatus status, Payload payload, Map<String, List<Object>> metadata) {
        return server(status, payload)
                .setMetadata(metadata);
    }

    public static ServerResponse server(ResponseStatus status, Payload payload, Map<String, List<Object>> metadata,
                                        String description) {
        return server(status, payload, metadata)
                .setDescription(description);
    }

    public static TransportResponse transport() {
        return new TransportResponse()
                .setMetadata(new HashMap<>())
                .setTrace(new Trace());
    }

    public static TransportResponse transport(ResponseStatus status) {
        return transport()
                .setStatus(status);
    }

    public static TransportResponse transport(ResponseStatus status, Payload payload) {
        return transport(status)
                .setPayload(payload);
    }

    public static TransportResponse transport(ResponseStatus status, Payload payload, String description) {
        return transport(status, payload)
                .setDescription(description);
    }

    public static TransportResponse transport(ResponseStatus status, String description) {
        return transport(status)
                .setDescription(description);
    }
}
