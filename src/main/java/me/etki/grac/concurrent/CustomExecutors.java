package me.etki.grac.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class CustomExecutors {

    public static DefaultScheduledExecutor newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return new DefaultScheduledExecutor(Executors.newSingleThreadScheduledExecutor(threadFactory));
    }

    public static DefaultScheduledExecutor newSingleThreadScheduledExecutor() {
        return newSingleThreadScheduledExecutor(Executors.defaultThreadFactory());
    }

    public static DefaultScheduledExecutor newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        return new DefaultScheduledExecutor(Executors.newScheduledThreadPool(corePoolSize, threadFactory));
    }

    public static DefaultScheduledExecutor newScheduledThreadPool(int corePoolSize) {
        return newScheduledThreadPool(corePoolSize, Executors.defaultThreadFactory());
    }
}
