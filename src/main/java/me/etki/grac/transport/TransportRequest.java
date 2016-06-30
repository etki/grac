package me.etki.grac.transport;

import com.google.common.net.MediaType;
import me.etki.grac.Action;
import me.etki.grac.common.ServerDetails;
import me.etki.grac.common.Metadata;
import me.etki.grac.common.Payload;

import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportRequest {

    private ServerDetails serverDetails;
    private Action action;
    private String resource;
    private List<MediaType> acceptedTypes;
    private List<String> acceptedLocales;
    private String clientIdentifier;
    private Long timeout;
    private Metadata metadata;
    private Payload payload;

    public ServerDetails getServerDetails() {
        return serverDetails;
    }

    public TransportRequest setServerDetails(ServerDetails serverDetails) {
        this.serverDetails = serverDetails;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public TransportRequest setAction(Action action) {
        this.action = action;
        return this;
    }

    public String getResource() {
        return resource;
    }

    public TransportRequest setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public List<MediaType> getAcceptedTypes() {
        return acceptedTypes;
    }

    public TransportRequest setAcceptedTypes(List<MediaType> acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
        return this;
    }

    public List<String> getAcceptedLocales() {
        return acceptedLocales;
    }

    public TransportRequest setAcceptedLocales(List<String> acceptedLocales) {
        this.acceptedLocales = acceptedLocales;
        return this;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public TransportRequest setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public TransportRequest setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public TransportRequest setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Payload getPayload() {
        return payload;
    }

    public TransportRequest setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }
}
