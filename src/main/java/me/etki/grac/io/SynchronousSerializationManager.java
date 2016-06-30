package me.etki.grac.io;

import com.google.common.net.MediaType;
import me.etki.grac.exception.SerializationException;
import me.etki.grac.utility.TypeSpec;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface SynchronousSerializationManager {

    <T> SerializationResult serialize(T object, MediaType mimeType) throws IOException, SerializationException;
    <T> DeserializationResult<T> deserialize(
            InputStream stream,
            MediaType mimeType,
            TypeSpec expectedType,
            List<TypeSpec> fallbackTypes)
            throws IOException, SerializationException;
}
