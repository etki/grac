package me.etki.grac.infrastructure.server;

import me.etki.grac.transport.server.Server;
import me.etki.grac.transport.server.ServerRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class FixedServerRegistry implements ServerRegistry {

    private static final Random RANDOM = new Random();

    private final List<Server> servers;

    public FixedServerRegistry(List<Server> servers) {
        Objects.requireNonNull(servers);
        this.servers = servers;
    }

    public FixedServerRegistry(Server... servers) {
        this(Arrays.asList(servers));
    }

    @Override
    public CompletableFuture<Optional<Server>> getServer() {
        if (servers.isEmpty()) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        return CompletableFuture.completedFuture(Optional.of(servers.get(RANDOM.nextInt(servers.size()))));
    }
}
