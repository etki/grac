package me.etki.grac.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface DelayService {

    CompletableFuture<Void> await(long delay, TimeUnit unit);
}
