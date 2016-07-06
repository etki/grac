package me.etki.grac.infrastructure;

import me.etki.grac.transport.Transport;
import me.etki.grac.transport.TransportRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportRegistries {

    public static TransportRegistry empty() {
        return new TransportRegistry(Collections.emptyList());
    }

    public static TransportRegistry of(List<Transport> transports) {
        return new TransportRegistry(transports);
    }

    public static TransportRegistry of(Transport... transports) {
        return of(Arrays.asList(transports));
    }
}
