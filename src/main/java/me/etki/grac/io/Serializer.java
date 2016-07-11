package me.etki.grac.io;

import com.google.common.net.MediaType;
import me.etki.grac.exception.SerializationException;
import me.etki.grac.utility.TypeSpec;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface Serializer {

    /**
     * Deserializes passed input stream into target object.
     *
     * This method may throw two types of exception: classic IOException and SerializationException. While IOException
     * is usually used to signal about any error that happens during stream read/write, this is not the case: this
     * method should only pass IOExceptions thrown by underlying stream itself, while all other exceptional situations -
     * that actually tell "i can't deserialize this" rather than "stream is broken" should be signaled about with
     * SerializationException. This difference influences overall control flow (serialization manager may treat I/O
     * exception as a signal to abort all operations, while SerializationException tells just to try another option).
     *
     * @param data Serialized object representation
     * @param mimeType Representation mime type
     * @param type Expected return type
     * @param <T> Expected return type
     * @return Deserialized object. {@code null} is a valid return value if it is exactly what was encoded in stream.
     * @throws SerializationException This exception should be thrown in case serializers cannot deserialize target
     * entity out of stream or even used against inappropriate format.
     * @throws IOException I/O exception should be thrown only in case of real I/O errors (not in case of impossibility
     * to correctly deserialize value). This exception should never be thrown if there was no direct I/O error.
     */
    <T> T deserialize(InputStream data, MediaType mimeType, TypeSpec type) throws IOException, SerializationException;

    /**
     * Serializes arbitrary object.
     *
     * @param object Object to be serialized.
     * @param mimeType Mime type for object to be serialized in.
     * @param <T> Object type.
     * @return Serialized object as a wrapped input stream.
     * @throws IOException Thrown in case of I/O errors
     * @throws SerializationException Thrown in case serializer can't serialize this specific instance
     */
    <T> SerializationResult serialize(T object, MediaType mimeType) throws IOException, SerializationException;

    boolean supports(MediaType mediaType);
}
