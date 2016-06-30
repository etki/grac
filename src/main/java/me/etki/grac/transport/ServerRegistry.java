package me.etki.grac.transport;

import me.etki.grac.common.ServerAddressProvider;
import me.etki.grac.common.ServerDetails;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ServerRegistry {

    private final ServerAddressProvider provider;
    private final TransportRegistry registry;
    private final LoadBalancingPolicy loadBalancingPolicy;

    public ServerRegistry(
            ServerAddressProvider provider,
            TransportRegistry registry,
            LoadBalancingPolicy loadBalancingPolicy) {

        this.provider = provider;
        this.registry = registry;
        this.loadBalancingPolicy = loadBalancingPolicy;
    }

    // todo check if server provider returns empty list or null and terminate early
    public CompletableFuture<Optional<ServerDetails>> getServer() {
        return provider
                .get()
                .thenApply(servers ->
                        servers.stream()
                                .filter(server -> registry.hasTransport(server.getProtocol()))
                                .collect(Collectors.toList())
                )
                .thenApply(servers -> servers.isEmpty() ? null : loadBalancingPolicy.getNext(servers))
                .thenApply(Optional::ofNullable);
    }
}
