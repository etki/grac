package me.etki.grac.infrastructure;

import me.etki.grac.application.ApplicationRequest;
import me.etki.grac.common.Action;
import me.etki.grac.policy.ForbiddingRetryPolicy;
import me.etki.grac.transport.Payload;
import me.etki.grac.transport.ServerRequest;
import me.etki.grac.transport.TransportRequest;
import me.etki.grac.transport.server.Server;
import me.etki.grac.utility.TypeSpec;

import java.util.Collections;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Requests {

    public static ServerRequest server() {
        return new ServerRequest()
                .setAcceptedMimeTypes(Collections.emptyList())
                .setAcceptedLocales(Collections.emptyList())
                .setMetadata(Collections.emptyMap())
                .setParameters(Collections.emptyMap());
    }

    public static ServerRequest server(Server server, Action action, String resource, Long timeout) {
        return server()
                .setServer(server)
                .setAction(action)
                .setResource(resource)
                .setTimeout(timeout);
    }

    public static ServerRequest server(Server server, Action action, String resource, Long timeout, Payload payload) {
        return server(server, action, resource, timeout)
                .setPayload(payload);
    }

    public static TransportRequest transport() {
        return new TransportRequest()
                .setParameters(Collections.emptyMap())
                .setMetadata(Collections.emptyMap())
                .setAcceptedMimeTypes(Collections.emptyList())
                .setAcceptedLocales(Collections.emptyList());
    }

    public static TransportRequest transport(Action action, String resource, Long timeout) {
        return transport()
                .setAction(action)
                .setResource(resource)
                .setTimeout(timeout)
                .setRetryPolicy(ForbiddingRetryPolicy.INSTANCE);
    }

    public static TransportRequest transport(Action action, String resource, Long timeout, Payload payload) {
        return transport(action, resource, timeout)
                .setPayload(payload);
    }

    public static <T> ApplicationRequest<T> application() {
        return new ApplicationRequest<T>()
                .setParameters(Collections.emptyMap())
                .setMetadata(Collections.emptyMap())
                .setAcceptedMimeTypes(Collections.emptyList())
                .setAcceptedLocales(Collections.emptyList())
                .setFallbackTypes(Collections.emptyList())
                .setRetryPolicy(ForbiddingRetryPolicy.INSTANCE)
                .setSerializationType(Commons.DEFAULT_MIME_TYPE)
                .setExpectedType(new TypeSpec(Void.class));
    }

    public static <T> ApplicationRequest<T> application(Action action, String resource, Long timeout) {
        return Requests
                .<T>application()
                .setAction(action)
                .setResource(resource)
                .setTimeout(timeout);
    }

    public static <T> ApplicationRequest<T> application(Action action, String resource, Long timeout, T payload) {
        return Requests
                .<T>application(action, resource, timeout)
                .setPayload(payload);
    }

    public static <T> ApplicationRequest<T> application(
            Action action,
            String resource,
            Long timeout,
            T payload,
            TypeSpec expectedType) {

        return application(action, resource, timeout, payload)
                .setExpectedType(expectedType);
    }

    public static <T> ApplicationRequest<T> application(
            Action action,
            String resource,
            Long timeout,
            TypeSpec expectedType) {

        return Requests
                .<T>application(action, resource, timeout)
                .setExpectedType(expectedType);
    }
}
