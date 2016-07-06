package me.etki.grac.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultDelayService implements DelayService {

    private static final CompletableFuture<Void> DELAY_ABSENCE = CompletableFuture.completedFuture(null);

    private final ScheduledExecutor scheduledExecutor;

    public DefaultDelayService(ScheduledExecutor scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
    }

    public CompletableFuture<Void> await(long delay, TimeUnit unit) {
        if (delay <= 0) {
            return DELAY_ABSENCE;
        }
        return scheduledExecutor.schedule(() -> null, delay, unit);
    }
}
