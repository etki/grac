package me.etki.grac.transport;

import me.etki.grac.common.ServerDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportRegistry {

    private final List<Transport> transports;

    public TransportRegistry(List<Transport> transports) {
        this.transports = transports;
    }

    // todo rename to something more suitable
    public List<ServerDetails> filterAddresses(Iterable<ServerDetails> addresses) {
        return StreamSupport.stream(addresses.spliterator(), false)
                .filter(address ->
                        transports.stream()
                                .filter(transport -> transport.supports(address.getProtocol()))
                                .findFirst()
                                .isPresent()
                )
                .collect(Collectors.toList());
    }

    public List<Transport> getTransports(String protocol) {
        return transports.stream().filter(transport -> transport.supports(protocol)).collect(Collectors.toList());
    }

    public Optional<Transport> getTransport(String protocol) {
        return transports.stream().filter(transport -> transport.supports(protocol)).findFirst();
    }

    public boolean hasTransport(String protocol) {
        return transports.stream().filter(transport -> transport.supports(protocol)).findFirst().isPresent();
    }
}
