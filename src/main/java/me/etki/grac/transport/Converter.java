package me.etki.grac.transport;

import me.etki.grac.common.ServerDetails;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Converter {

    public static TransportRequest assembleTransportRequest(TLRequest request, ServerDetails details) {
        return new TransportRequest()
                .setServerDetails(details)
                .setAction(request.getAction())
                .setResource(request.getResource())
                .setAcceptedTypes(request.getAcceptedTypes())
                .setAcceptedLocales(request.getAcceptedLocales())
                .setClientIdentifier(request.getClientIdentifier())
                .setTimeout(request.getTimeout())
                .setMetadata(request.getMetadata())
                .setPayload(request.getPayload());
    }

    public static TLResponse assembleTLResponse(TransportResponse response, RequestContext context) {
        return new TLResponse()
                .setStatus(response.getStatus())
                .setDescription(response.getDescription())
                .setMetadata(response.getMetadata())
                .setPayload(response.getPayload())
                .setTrace(context.getTrace());
    }
}
