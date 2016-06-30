package me.etki.grac.common;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class FixedServerAddressProvider implements ServerAddressProvider {

    private final List<ServerDetails> addresses;

    public FixedServerAddressProvider(List<ServerDetails> addresses) {
        this.addresses = addresses;
    }

    public FixedServerAddressProvider(ServerDetails... addresses) {
        this(Arrays.asList(addresses));
    }

    @Override
    public CompletableFuture<List<ServerDetails>> get() {
        return CompletableFuture.completedFuture(addresses);
    }

    @Override
    public String toString() {
        return "FixedServerAddressProvider {server addresses=" + addresses + "}";
    }
}
