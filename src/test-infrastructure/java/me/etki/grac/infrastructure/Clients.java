package me.etki.grac.infrastructure;

import com.google.common.net.MediaType;
import me.etki.grac.Client;
import me.etki.grac.ClientBuilder;
import me.etki.grac.io.Serializer;
import me.etki.grac.transport.Transport;
import me.etki.grac.transport.server.ServerProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Clients {

    public static Client of(ServerProvider provider, Collection<Transport> transports) {
        return of(provider, transports, Collections.emptyList());
    }

    public static Client of(ServerProvider provider, Transport... transports) {
        return of(provider, Arrays.asList(transports));
    }

    public static Client of(
            ServerProvider provider,
            Collection<Transport> transports,
            Serializer... serializers) {

        return of(provider, transports, Arrays.asList(serializers), null);
    }

    public static Client of(
            ServerProvider provider,
            Collection<Transport> transports,
            Collection<Serializer> serializers) {

        return of(provider, transports, serializers, null);
    }

    public static Client of(
            ServerProvider provider,
            Collection<Transport> transports,
            Collection<Serializer> serializers,
            MediaType serializationMimeType) {

        ClientBuilder builder = ClientBuilders.regular(provider, transports, serializers);
        if (serializationMimeType != null) {
            builder.withDefaultSerializationType(serializationMimeType);
        }
        return builder.build();
    }

    public static Client of(
            ServerProvider provider,
            Collection<Transport> transports,
            Serializer serializer,
            MediaType serializationMimeType) {

        return of(provider, transports, Collections.singleton(serializer), serializationMimeType);
    }

    public static Client of(
            ServerProvider provider,
            Transport transport,
            Collection<Serializer> serializers,
            MediaType serializationMimeType) {

        return of(provider, Collections.singleton(transport), serializers, serializationMimeType);
    }

    public static Client of(
            ServerProvider provider,
            Transport transport,
            Serializer serializer,
            MediaType serializationMimeType) {

        return of(provider, Collections.singleton(transport), serializer, serializationMimeType);
    }
}
