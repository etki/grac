package me.etki.grac.infrastructure;

import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.policy.RandomHostLoadBalancingPolicy;
import me.etki.grac.transport.server.Server;
import me.etki.grac.transport.server.ServerRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ServerRegistries {

    public static final ServerRegistry EMPTY = empty();
    public static final ServerRegistry GITHUB = github();
    public static final ServerRegistry LOCALHOST = localhost();

    public static ServerRegistry empty() {
        return () -> CompletableFutures.completed(Optional.empty());
    }

    public static ServerRegistry random(List<Server> servers) {
        Supplier<Server> supplier = () ->
                servers.isEmpty() ? null : RandomHostLoadBalancingPolicy.INSTANCE.getNext(servers);
        return () -> CompletableFutures.completed(Optional.of(supplier.get()));
    }

    public static ServerRegistry random(Server... servers) {
        return random(Arrays.asList(servers));
    }

    public static ServerRegistry github() {
        return random(Servers.github());
    }

    public static ServerRegistry localhost() {
        return random(Servers.localhost());
    }
}
