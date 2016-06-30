package me.etki.grac.common;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface ServerAddressProvider {

    CompletableFuture<List<ServerDetails>> get();
}
