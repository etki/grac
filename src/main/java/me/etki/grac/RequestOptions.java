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
    
    public static RequestOptions copy(RequestOptions source) {
        return new RequestOptions()
                .setRetryPolicy(source.getRetryPolicy())
                .setSerializationType(source.getSerializationType())
                .setAcceptedMimeTypes(source.getAcceptedMimeTypes())
                .setAcceptedLocales(source.getAcceptedLocales())
                .setFallbackObjectTypes(source.getFallbackObjectTypes())
                .setTimeout(source.getTimeout())
                .setClientIdentifier(source.getClientIdentifier());
    }

    public static RequestOptions merge(RequestOptions a, RequestOptions b) {
        
        RequestOptions result = copy(a);
        if (result.getRetryPolicy() == null) {
            result.setRetryPolicy(b.getRetryPolicy());
        }
        if (result.getSerializationType() == null) {
            result.setSerializationType(b.getSerializationType());
        }
        if (result.getAcceptedMimeTypes() == null) {
            result.setAcceptedMimeTypes(b.getAcceptedMimeTypes());
        }
        if (result.getAcceptedLocales() == null) {
            result.setAcceptedLocales(b.getAcceptedLocales());
        }
        if (result.getFallbackObjectTypes() == null) {
            result.setFallbackObjectTypes(b.getFallbackObjectTypes());
        }
        if (result.getTimeout() == null) {
            result.setTimeout(b.getTimeout());
        }
        if (result.getClientIdentifier() == null) {
            result.setClientIdentifier(b.getClientIdentifier());
        }
        return result;
    }
}
