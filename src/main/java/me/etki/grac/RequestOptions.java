package me.etki.grac;

import com.google.common.net.MediaType;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.utility.TypeSpec;

import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class RequestOptions {

    private RetryPolicy retryPolicy;
    private MediaType serializationType;
    private List<MediaType> acceptedMimeTypes;
    private List<String> acceptedLocales;
    private List<TypeSpec> fallbackObjectTypes;
    private Long timeout;
    private String clientIdentifier;

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public RequestOptions setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public MediaType getSerializationType() {
        return serializationType;
    }

    public RequestOptions setSerializationType(MediaType serializationType) {
        this.serializationType = serializationType;
        return this;
    }

    public List<MediaType> getAcceptedMimeTypes() {
        return acceptedMimeTypes;
    }

    public RequestOptions setAcceptedMimeTypes(List<MediaType> acceptedMimeTypes) {
        this.acceptedMimeTypes = acceptedMimeTypes;
        return this;
    }

    public List<TypeSpec> getFallbackObjectTypes() {
        return fallbackObjectTypes;
    }

    public RequestOptions setFallbackObjectTypes(List<TypeSpec> fallbackObjectTypes) {
        this.fallbackObjectTypes = fallbackObjectTypes;
        return this;
    }

    public List<String> getAcceptedLocales() {
        return acceptedLocales;
    }

    public RequestOptions setAcceptedLocales(List<String> acceptedLocales) {
        this.acceptedLocales = acceptedLocales;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public RequestOptions setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public RequestOptions setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
        return this;
    }

    public RequestOptions copy() {
        return new RequestOptions()
                .setRetryPolicy(retryPolicy)
                .setAcceptedMimeTypes(acceptedMimeTypes)
                .setAcceptedLocales(acceptedLocales)
                .setFallbackObjectTypes(fallbackObjectTypes)
                .setTimeout(timeout);
    }

    public static RequestOptions merge(RequestOptions target, RequestOptions source) {

        if (target.getRetryPolicy() == null) {
            target.setRetryPolicy(source.getRetryPolicy());
        }
        if (target.getAcceptedMimeTypes() == null) {
            target.setAcceptedMimeTypes(source.getAcceptedMimeTypes());
        }
        if (target.getAcceptedLocales() == null) {
            target.setAcceptedLocales(source.getAcceptedLocales());
        }
        if (target.getTimeout() == null) {
            target.setTimeout(source.getTimeout());
        }
        if (target.getFallbackObjectTypes() == null) {
            target.setFallbackObjectTypes(source.getFallbackObjectTypes());
        }
        if (target.getSerializationType() == null) {
            target.setSerializationType(source.getSerializationType());
        }
        return target;
    }
}
