package me.etki.grac.io;

import com.google.common.net.MediaType;
import me.etki.grac.concurrent.CompletableFutureFactory;
import me.etki.grac.utility.TypeSpec;

import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class AsyncSerializer {

    private final Serializer serializer;
    private final CompletableFutureFactory completableFutureFactory;

    public AsyncSerializer(Serializer serializer, CompletableFutureFactory completableFutureFactory) {
        Objects.requireNonNull(serializer);
        Objects.requireNonNull(completableFutureFactory);
        this.serializer = serializer;
        this.completableFutureFactory = completableFutureFactory;
    }

    public <T> CompletableFuture<T> deserialize(InputStream data, MediaType mediaType, TypeSpec typeSpec) {
        return completableFutureFactory.call(() -> serializer.<T>deserialize(data, mediaType, typeSpec));
    }

    public <T> CompletableFuture<SerializationResult> serialize(T object, MediaType mediaType) {
        return completableFutureFactory.call(() -> serializer.serialize(object, mediaType));
    }

    public boolean supports(MediaType mediaType) {
        return serializer.supports(mediaType);
    }
}
