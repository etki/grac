package me.etki.grac.transport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Transport> getTransports(String protocol) {
        return transports.stream().filter(transport -> transport.supports(protocol)).collect(Collectors.toList());
    }

    public Optional<Transport> getTransport(String protocol) {
        return transports.stream().filter(transport -> transport.supports(protocol)).findFirst();
    }

    public boolean hasTransport(String protocol) {
        return getTransport(protocol).isPresent();
    }
}
