package me.etki.grac;

import me.etki.grac.common.Metadata;
import me.etki.grac.common.ResponseStatus;
import me.etki.grac.transport.TLRequest;
import me.etki.grac.transport.TLResponse;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class AssembledResponse<I, O> {

    private ResponseStatus status;
    private String description;
    private Metadata metadata;
    private O result;
    private Object altResult;

    private AssembledRequest<I> request;
    private TLRequest transportLayerRequest;
    private TLResponse transportLayerResponse;

    public ResponseStatus getStatus() {
        return status;
    }

    public AssembledResponse<I, O> setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AssembledResponse<I, O> setDescription(String description) {
        this.description = description;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public AssembledResponse<I, O> setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public O getResult() {
        return result;
    }

    public AssembledResponse<I, O> setResult(O result) {
        this.result = result;
        return this;
    }

    public Object getAltResult() {
        return altResult;
    }

    public AssembledResponse<I, O> setAltResult(Object altResult) {
        this.altResult = altResult;
        return this;
    }

    public AssembledRequest<I> getRequest() {
        return request;
    }

    public AssembledResponse<I, O> setRequest(AssembledRequest<I> request) {
        this.request = request;
        return this;
    }

    public TLRequest getTransportLayerRequest() {
        return transportLayerRequest;
    }

    public AssembledResponse<I, O> setTransportLayerRequest(TLRequest transportLayerRequest) {
        this.transportLayerRequest = transportLayerRequest;
        return this;
    }

    public TLResponse getTransportLayerResponse() {
        return transportLayerResponse;
    }

    public AssembledResponse<I, O> setTransportLayerResponse(TLResponse transportLayerResponse) {
        this.transportLayerResponse = transportLayerResponse;
        return this;
    }
}
