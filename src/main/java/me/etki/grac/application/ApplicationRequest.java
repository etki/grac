package me.etki.grac.application;

import com.google.common.net.MediaType;
import me.etki.grac.common.Action;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.utility.StaticValidator;
import me.etki.grac.utility.TypeSpec;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ApplicationRequest<T> {

    private Action action;
    private String resource;
    private Map<String, List<Object>> parameters;
    private Map<String, List<Object>> metadata;
    private MediaType serializationType;
    private TypeSpec expectedType;
    private List<TypeSpec> fallbackTypes;
    private List<String> acceptedLocales;
    private List<MediaType> acceptedMimeTypes;
    private RetryPolicy retryPolicy;
    private Long timeout;
    private T payload;
    private String clientIdentifier;

    public Action getAction() {
        return action;
    }

    public ApplicationRequest<T> setAction(Action action) {
        this.action = action;
        return this;
    }

    public String getResource() {
        return resource;
    }

    public ApplicationRequest<T> setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public Map<String, List<Object>> getParameters() {
        return parameters;
    }

    public ApplicationRequest<T> setParameters(Map<String, List<Object>> parameters) {
        this.parameters = parameters;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public ApplicationRequest<T> setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public TypeSpec getExpectedType() {
        return expectedType;
    }

    public ApplicationRequest<T> setExpectedType(TypeSpec expectedType) {
        this.expectedType = expectedType;
        return this;
    }

    public List<TypeSpec> getFallbackTypes() {
        return fallbackTypes;
    }

    public ApplicationRequest<T> setFallbackTypes(List<TypeSpec> fallbackTypes) {
        this.fallbackTypes = fallbackTypes;
        return this;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public ApplicationRequest<T> setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public MediaType getSerializationType() {
        return serializationType;
    }

    public ApplicationRequest<T> setSerializationType(MediaType serializationType) {
        this.serializationType = serializationType;
        return this;
    }

    public List<String> getAcceptedLocales() {
        return acceptedLocales;
    }

    public ApplicationRequest<T> setAcceptedLocales(List<String> acceptedLocales) {
        this.acceptedLocales = acceptedLocales;
        return this;
    }

    public List<MediaType> getAcceptedMimeTypes() {
        return acceptedMimeTypes;
    }

    public ApplicationRequest<T> setAcceptedMimeTypes(List<MediaType> acceptedMimeTypes) {
        this.acceptedMimeTypes = acceptedMimeTypes;
        return this;
    }

    public Map<String, List<Object>> getMetadata() {
        return metadata;
    }

    public ApplicationRequest<T> setMetadata(Map<String, List<Object>> metadata) {
        this.metadata = metadata;
        return this;
    }

    public Optional<String> getClientIdentifier() {
        return Optional.ofNullable(clientIdentifier);
    }

    public ApplicationRequest<T> setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
        return this;
    }

    public Optional<T> getPayload() {
        return Optional.ofNullable(payload);
    }

    public ApplicationRequest<T> setPayload(T payload) {
        this.payload = payload;
        return this;
    }

    public void validate() {
        StaticValidator.requireNonNull(action, "Action is not set");
        StaticValidator.requireNonNull(resource, "Resource is not set");
        StaticValidator.requireNonNull(parameters, "Parameters are not set, some value (even empty) is required");
        StaticValidator.requireNonNull(metadata, "Metadata is not set, some value (even empty) is required");
        StaticValidator.verifyState(payload == null || serializationType != null,
                "Serialization type could not be null if payload is not null");
        StaticValidator.requireNonNull(expectedType, "Expected type is not set");
        StaticValidator.requireNonNull(fallbackTypes,
                "Fallback types are not set, some value (even empty) is required");
        StaticValidator.requireNonNull(acceptedLocales,
                "Accepted locales are not set, some value (even empty) is required");
        StaticValidator.requireNonNull(acceptedMimeTypes,
                "Accepted types are not set, some value (even empty) is required");
        StaticValidator.requireNonNull(retryPolicy, "Retry policy is not set");
        StaticValidator.requireNonNull(timeout, "Timeout is not set");
    }
}
