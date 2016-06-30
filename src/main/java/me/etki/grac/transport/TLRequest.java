package me.etki.grac.transport;

import com.google.common.net.MediaType;
import me.etki.grac.Action;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.common.Metadata;
import me.etki.grac.common.Payload;

import java.util.List;

/**
 * TL stands for transport layer.
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TLRequest {

    private Action action;
    private String resource;
    private List<MediaType> acceptedTypes;
    private List<String> acceptedLocales;
    private Long timeout;
    private Payload payload;
    private Metadata metadata;
    private RetryPolicy retryPolicy;
    private LoadBalancingPolicy loadBalancingPolicy;
    private String clientIdentifier;

    public String getResource() {
        return resource;
    }

    public TLRequest setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public TLRequest setAction(Action action) {
        this.action = action;
        return this;
    }

    public List<MediaType> getAcceptedTypes() {
        return acceptedTypes;
    }

    public TLRequest setAcceptedTypes(List<MediaType> acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
        return this;
    }

    public List<String> getAcceptedLocales() {
        return acceptedLocales;
    }

    public TLRequest setAcceptedLocales(List<String> acceptedLocales) {
        this.acceptedLocales = acceptedLocales;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public TLRequest setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Payload getPayload() {
        return payload;
    }

    public TLRequest setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public TLRequest setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public TLRequest setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public LoadBalancingPolicy getLoadBalancingPolicy() {
        return loadBalancingPolicy;
    }

    public TLRequest setLoadBalancingPolicy(LoadBalancingPolicy loadBalancingPolicy) {
        this.loadBalancingPolicy = loadBalancingPolicy;
        return this;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public TLRequest setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
        return this;
    }
}
