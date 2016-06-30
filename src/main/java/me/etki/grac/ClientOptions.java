package me.etki.grac;

import com.google.common.net.MediaType;
import me.etki.grac.policy.RetryPolicy;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ClientOptions {

    private MediaType defaultSerializationType;
    private RetryPolicy defaultRetryPolicy;

    public MediaType getDefaultSerializationType() {
        return defaultSerializationType;
    }

    public ClientOptions setDefaultSerializationType(MediaType defaultSerializationType) {
        this.defaultSerializationType = defaultSerializationType;
        return this;
    }

    public RetryPolicy getDefaultRetryPolicy() {
        return defaultRetryPolicy;
    }

    public ClientOptions setDefaultRetryPolicy(RetryPolicy defaultRetryPolicy) {
        this.defaultRetryPolicy = defaultRetryPolicy;
        return this;
    }
}
