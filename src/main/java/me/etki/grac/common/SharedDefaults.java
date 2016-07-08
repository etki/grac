package me.etki.grac.common;

import com.google.common.net.MediaType;
import me.etki.grac.RequestOptions;
import me.etki.grac.concurrent.DefaultScheduledExecutor;
import me.etki.grac.policy.InstantRetryPolicy;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.policy.LoadBalancingPolicy;
import me.etki.grac.policy.RandomHostLoadBalancingPolicy;
import me.etki.grac.concurrent.CustomExecutors;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class SharedDefaults {

    public static final String VERSION = "0.1.0";
    public static final long DEFAULT_REQUEST_TIMEOUT = 10_000;
    public static final String DEFAULT_CLIENT_IDENTIFIER = "GRAC/" + VERSION;
    public static final RetryPolicy DEFAULT_RETRY_POLICY = new InstantRetryPolicy();
    public static final MediaType JSON_MIME_TYPE = MediaType.JSON_UTF_8;
    public static final LoadBalancingPolicy DEFAULT_LOAD_BALANCING_POLICY = new RandomHostLoadBalancingPolicy();
    public static final RequestOptions DEFAULT_REQUEST_OPTIONS = new RequestOptions()
            .setTimeout(DEFAULT_REQUEST_TIMEOUT)
            .setRetryPolicy(DEFAULT_RETRY_POLICY)
            .setAcceptedMimeTypes(Collections.emptyList())
            .setFallbackObjectTypes(Collections.emptyList());
    public static final int DEFAULT_INPUT_STREAM_MARK_LIMIT = 32768;
    public static final int DEFAULT_REQUEST_QUEUE_SIZE = 200;
    public static final int DEFAULT_CONCURRENT_REQUEST_LIMIT = 20;

    private static volatile DefaultScheduledExecutor scheduler;

    public static synchronized DefaultScheduledExecutor getDefaultScheduler() {
        if (scheduler == null) {
            scheduler = CustomExecutors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(Thread.currentThread().getThreadGroup(), runnable);
                thread.setDaemon(false);
                thread.setName("grac:default-scheduler:worker-1");
                return thread;
            });
        }
        return scheduler;
    }

    public static synchronized Executor getDefaultExecutor() {
        return ForkJoinPool.commonPool();
    }
}
