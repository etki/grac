package me.etki.grac;

import com.google.common.net.MediaType;
import me.etki.grac.common.Metadata;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.utility.TypeSpec;

import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class AssembledRequest<T> {

    private String resource;
    private Action action;
    private TypeSpec expectedType;
    private List<TypeSpec> fallbackTypes;
    private Long timeout;
    private RetryPolicy retryPolicy;
    private MediaType serializationType;
    private List<MediaType> acceptedTypes;
    private List<String> acceptedLocales;
    private T payload;
    private Metadata metadata;

    public String getResource() {
        return resource;
    }

    public AssembledRequest<T> setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public AssembledRequest<T> setAction(Action action) {
        this.action = action;
        return this;
    }

    public TypeSpec getExpectedType() {
        return expectedType;
    }

    public AssembledRequest<T> setExpectedType(TypeSpec expectedType) {
        this.expectedType = expectedType;
        return this;
    }

    public List<TypeSpec> getFallbackTypes() {
        return fallbackTypes;
    }

    public AssembledRequest<T> setFallbackTypes(List<TypeSpec> fallbackTypes) {
        this.fallbackTypes = fallbackTypes;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public AssembledRequest<T> setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public List<MediaType> getAcceptedTypes() {
        return acceptedTypes;
    }

    public MediaType getSerializationType() {
        return serializationType;
    }

    public AssembledRequest<T> setSerializationType(MediaType serializationType) {
        this.serializationType = serializationType;
        return this;
    }

    public AssembledRequest<T> setAcceptedTypes(List<MediaType> acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
        return this;
    }

    public List<String> getAcceptedLocales() {
        return acceptedLocales;
    }

    public AssembledRequest<T> setAcceptedLocales(List<String> acceptedLocales) {
        this.acceptedLocales = acceptedLocales;
        return this;
    }

    public T getPayload() {
        return payload;
    }

    public AssembledRequest<T> setPayload(T payload) {
        this.payload = payload;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public AssembledRequest<T> setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public AssembledRequest<T> setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }
}
