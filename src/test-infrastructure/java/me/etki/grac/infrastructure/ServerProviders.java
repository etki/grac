package me.etki.grac.infrastructure;

import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.transport.server.Server;
import me.etki.grac.transport.server.ServerProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ServerProviders {

    public static ServerProvider empty() {
        return () -> CompletableFutures.completed(Collections.emptyList());
    }

    public static ServerProvider of(Server... servers) {
        return of(Arrays.asList(servers));
    }

    public static ServerProvider of(List<Server> servers) {
        return () -> CompletableFutures.completed(servers);
    }

    public static ServerProvider localhost() {
        return of(Servers.localhost());
    }

    public static ServerProvider github() {
        return of(Servers.github());
    }
}
