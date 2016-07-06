package me.etki.grac.transport;

import com.google.common.net.MediaType;
import me.etki.grac.common.Action;
import me.etki.grac.transport.server.Server;
import me.etki.grac.utility.StaticValidator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ServerRequest {

    private Server server;
    private Action action;
    private String resource;
    private Map<String, List<Object>> parameters = Collections.emptyMap();
    private Map<String, List<Object>> metadata = Collections.emptyMap();
    private List<MediaType> acceptedMimeTypes = Collections.emptyList();
    private List<String> acceptedLocales = Collections.emptyList();
    private Long timeout;
    private Payload payload;
    private String clientIdentifier;

    public Server getServer() {
        return server;
    }

    public ServerRequest setServer(Server server) {
        Objects.requireNonNull(server);
        this.server = server;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public ServerRequest setAction(Action action) {
        Objects.requireNonNull(action);
        this.action = action;
        return this;
    }

    public String getResource() {
        return resource;
    }

    public ServerRequest setResource(String resource) {
        Objects.requireNonNull(resource);
        this.resource = resource;
        return this;
    }

    public Map<String, List<Object>> getParameters() {
        return parameters;
    }

    public ServerRequest setParameters(Map<String, List<Object>> parameters) {
        this.parameters = parameters;
        return this;
    }

    public List<MediaType> getAcceptedMimeTypes() {
        Objects.requireNonNull(acceptedLocales);
        return acceptedMimeTypes;
    }

    public ServerRequest setAcceptedMimeTypes(List<MediaType> acceptedMimeTypes) {
        Objects.requireNonNull(acceptedMimeTypes);
        this.acceptedMimeTypes = acceptedMimeTypes;
        return this;
    }

    public List<String> getAcceptedLocales() {
        return acceptedLocales;
    }

    public ServerRequest setAcceptedLocales(List<String> acceptedLocales) {
        Objects.requireNonNull(acceptedLocales);
        this.acceptedLocales = acceptedLocales;
        return this;
    }

    public Optional<String> getClientIdentifier() {
        return Optional.ofNullable(clientIdentifier);
    }

    public ServerRequest setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public ServerRequest setTimeout(Long timeout) {
        Objects.requireNonNull(timeout);
        this.timeout = timeout;
        return this;
    }

    public Map<String, List<Object>> getMetadata() {
        return metadata;
    }

    public ServerRequest setMetadata(Map<String, List<Object>> metadata) {
        this.metadata = metadata;
        return this;
    }

    public Optional<Payload> getPayload() {
        return Optional.ofNullable(payload);
    }

    public ServerRequest setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public ServerRequest validate() {
        StaticValidator.requireNonNull(server, "Server could not be null");
        StaticValidator.requireNonNull(action, "Action could not be null");
        StaticValidator.requireNonNull(resource, "Resource could not be null");
        StaticValidator.requireNonNull(parameters, "Parameters could not be null (empty value allowed)");
        StaticValidator.requireNonNull(metadata, "Metadata could not be null (empty value is allowed)");
        StaticValidator.requireNonNull(acceptedLocales, "Accepted locales could not be null (empty value is allowed");
        StaticValidator.requireNonNull(acceptedMimeTypes,
                "Accepted mime types could not be null (empty value is allowed");
        StaticValidator.requireNonNull(timeout, "Timeout could not be null");
        return this;
    }

    @Override
    public String toString() {
        return "ServerRequest {server={" + server + "}, action=" + action + ", resource='" + resource + "', " +
                "timeout=" + timeout + ", payload={" + payload + "}}";
    }
}
