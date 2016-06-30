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
    private List<MediaType> acceptedTypes;
    private List<String> acceptedLocales;
    private List<TypeSpec> fallbackTypes;
    private Long timeout;

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

    public List<MediaType> getAcceptedTypes() {
        return acceptedTypes;
    }

    public RequestOptions setAcceptedTypes(List<MediaType> acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
        return this;
    }

    public List<TypeSpec> getFallbackTypes() {
        return fallbackTypes;
    }

    public RequestOptions setFallbackTypes(List<TypeSpec> fallbackTypes) {
        this.fallbackTypes = fallbackTypes;
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

    public RequestOptions copy() {
        return new RequestOptions()
                .setRetryPolicy(retryPolicy)
                .setAcceptedTypes(acceptedTypes)
                .setAcceptedLocales(acceptedLocales)
                .setFallbackTypes(fallbackTypes)
                .setTimeout(timeout);
    }

    public static RequestOptions merge(RequestOptions target, RequestOptions source) {

        if (target.getRetryPolicy() == null) {
            target.setRetryPolicy(source.getRetryPolicy());
        }
        if (target.getAcceptedTypes() == null) {
            target.setAcceptedTypes(source.getAcceptedTypes());
        }
        if (target.getAcceptedLocales() == null) {
            target.setAcceptedLocales(source.getAcceptedLocales());
        }
        if (target.getTimeout() == null) {
            target.setTimeout(source.getTimeout());
        }
        if (target.getFallbackTypes() == null) {
            target.setFallbackTypes(source.getFallbackTypes());
        }
        return target;
    }
}
