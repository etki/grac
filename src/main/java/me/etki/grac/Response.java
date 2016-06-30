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
public class Response<T> {

    private ResponseStatus status;
    private String description;
    private T result;
    private Object altResult;
    private Metadata metadata;
    private TLRequest transportRequest;
    private TLResponse transportResponse;

    public ResponseStatus getStatus() {
        return status;
    }

    public Response<T> setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Response<T> setDescription(String description) {
        this.description = description;
        return this;
    }

    public T getResult() {
        return result;
    }

    public Response<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public Object getAltResult() {
        return altResult;
    }

    public Response<T> setAltResult(Object altResult) {
        this.altResult = altResult;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Response<T> setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public TLRequest getTransportRequest() {
        return transportRequest;
    }

    public Response<T> setTransportRequest(TLRequest transportRequest) {
        this.transportRequest = transportRequest;
        return this;
    }

    public TLResponse getTransportResponse() {
        return transportResponse;
    }

    public Response<T> setTransportResponse(TLResponse transportResponse) {
        this.transportResponse = transportResponse;
        return this;
    }
}
