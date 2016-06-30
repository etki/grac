package me.etki.grac.common;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class FailedRequestDetails {

    private ServerDetails serverDetails;
    private Metadata requestMetadata;
    private Metadata responseMetadata;

    public ServerDetails getServerDetails() {
        return serverDetails;
    }

    public FailedRequestDetails setServerDetails(ServerDetails serverDetails) {
        this.serverDetails = serverDetails;
        return this;
    }

    public Metadata getRequestMetadata() {
        return requestMetadata;
    }

    public FailedRequestDetails setRequestMetadata(Metadata requestMetadata) {
        this.requestMetadata = requestMetadata;
        return this;
    }

    public Metadata getResponseMetadata() {
        return responseMetadata;
    }

    public FailedRequestDetails setResponseMetadata(Metadata responseMetadata) {
        this.responseMetadata = responseMetadata;
        return this;
    }
}
