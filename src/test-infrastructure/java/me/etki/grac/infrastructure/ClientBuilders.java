package me.etki.grac.infrastructure;

import com.google.common.net.MediaType;
import me.etki.grac.ClientBuilder;
import me.etki.grac.io.Serializer;
import me.etki.grac.transport.Transport;
import me.etki.grac.transport.server.ServerProvider;

import java.util.Collection;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ClientBuilders {

    public static ClientBuilder localhost() {
        return new ClientBuilder().withServiceAddressProvider(ServerProviders.localhost());
    }

    public static ClientBuilder github() {
        return new ClientBuilder().withServiceAddressProvider(ServerProviders.github());
    }

    public static ClientBuilder regular(
            ServerProvider provider,
            Collection<Transport> transports,
            Collection<Serializer> serializers) {

        return new ClientBuilder()
                .withServiceAddressProvider(provider)
                .withTransports(transports)
                .withSerializers(serializers);
    }

    public static ClientBuilder regular(
            ServerProvider provider,
            Collection<Transport> transports,
            Collection<Serializer> serializers,
            MediaType serializationType) {

        return regular(provider, transports, serializers)
                .withDefaultSerializationType(serializationType);
    }
}
