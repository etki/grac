package me.etki.grac.transport.server;

import me.etki.grac.policy.LoadBalancingPolicy;
import me.etki.grac.transport.TransportRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Todo: expel transport registry from here
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultServerRegistry implements ServerRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServerRegistry.class);

    private final ServerProvider provider;
    private final TransportRegistry registry;
    private final LoadBalancingPolicy loadBalancingPolicy;

    public DefaultServerRegistry(
            ServerProvider provider,
            TransportRegistry registry,
            LoadBalancingPolicy loadBalancingPolicy) {

        this.provider = provider;
        this.registry = registry;
        this.loadBalancingPolicy = loadBalancingPolicy;
    }

    @Override
    public CompletableFuture<Optional<Server>> getServer() {
        LOGGER.debug("Retrieving next server definition");
        return provider
                .get()
                .thenApply(this::applyTransportFilter)
                .thenApply(this::applyLoadBalancingPolicy);
    }

    private List<Server> applyTransportFilter(List<Server> servers) {
        if (servers.isEmpty()) {
            LOGGER.debug("Server definition provider returned no server definitions, short-circuiting");
            return servers;
        }
        LOGGER.debug("Fetched {} server definitions out of server definition provider", servers.size());
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Fetched servers: {}", servers);
        }
        List<Server> filtered = servers.stream()
                .filter(server -> registry.hasTransport(server.getProtocol()))
                .collect(Collectors.toList());
        LOGGER.debug("Filtered out servers that don't have corresponding transport to support, {} left",
                filtered.size());
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Remaining servers after transport filter application: {}", filtered);
        }
        return filtered;
    }

    private Optional<Server> applyLoadBalancingPolicy(List<Server> servers) {
        Server server = loadBalancingPolicy.getNext(servers);
        LOGGER.debug("Load balancing policy {} selected next server: {}", loadBalancingPolicy, server);
        return Optional.ofNullable(server);
    }
}
