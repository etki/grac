package me.etki.grac.transport;

import com.google.common.net.MediaType;
import me.etki.grac.common.Action;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.utility.StaticValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * todo: convert to interface, add isIdempotent() method
 * todo: add application-to-transport layer converter interface so users could convert requests using their own logic
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportRequest {

    private Action action;
    private String resource;
    private Map<String, List<Object>> parameters;
    private Map<String, List<Object>> metadata;
    private List<MediaType> acceptedMimeTypes;
    private List<String> acceptedLocales;
    private RetryPolicy retryPolicy;
    private Long timeout;
    private Payload payload;
    private String clientIdentifier;

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

    public Map<String, List<Object>> getParameters() {
        return parameters;
    }

    public TransportRequest setParameters(Map<String, List<Object>> parameters) {
        this.parameters = parameters;
        return this;
    }

    public List<MediaType> getAcceptedMimeTypes() {
        return acceptedMimeTypes;
    }

    public TransportRequest setAcceptedMimeTypes(List<MediaType> acceptedMimeTypes) {
        this.acceptedMimeTypes = acceptedMimeTypes;
        return this;
    }

    public List<String> getAcceptedLocales() {
        return acceptedLocales;
    }

    public TransportRequest setAcceptedLocales(List<String> acceptedLocales) {
        this.acceptedLocales = acceptedLocales;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public TransportRequest setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Map<String, List<Object>> getMetadata() {
        return metadata;
    }

    public TransportRequest setMetadata(Map<String, List<Object>> metadata) {
        this.metadata = metadata;
        return this;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public TransportRequest setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public Optional<Payload> getPayload() {
        return Optional.ofNullable(payload);
    }

    public TransportRequest setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public Optional<String> getClientIdentifier() {
        return Optional.ofNullable(clientIdentifier);
    }

    public TransportRequest setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
        return this;
    }

    public void validate() {
        StaticValidator.requireNonNull(action, "Action could not be null");
        StaticValidator.requireNonNull(resource, "Resource could not be null");
        StaticValidator.requireNonNull(parameters, "Parameters could not be null (empty value allowed)");
        StaticValidator.requireNonNull(metadata, "Metadata could not be null (empty value allowed)");
        StaticValidator.requireNonNull(acceptedMimeTypes, "Accepted types could not be null (empty value allowed)");
        StaticValidator.requireNonNull(acceptedLocales, "Accepted locales could not be null (empty value allowed)");
        StaticValidator.requireNonNull(retryPolicy, "Retry policy could not be null");
        StaticValidator.requireNonNull(timeout, "Timeout could not be null");
    }

    @Override
    public String toString() {
        return "TransportRequest {action=" + action + ", resource='" + resource + "', timeout=" + timeout + "}";
    }
}
