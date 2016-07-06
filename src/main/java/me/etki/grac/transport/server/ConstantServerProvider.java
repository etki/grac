package me.etki.grac.transport.server;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ConstantServerProvider implements ServerProvider {

    private final List<Server> addresses;

    public ConstantServerProvider(List<Server> addresses) {
        this.addresses = addresses;
    }

    public ConstantServerProvider(Server... addresses) {
        this(Arrays.asList(addresses));
    }

    @Override
    public CompletableFuture<List<Server>> get() {
        return CompletableFuture.completedFuture(addresses);
    }

    @Override
    public String toString() {
        return "ConstantServerProvider {server addresses=" + addresses + "}";
    }
}
