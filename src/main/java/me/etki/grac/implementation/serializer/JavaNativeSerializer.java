package me.etki.grac.implementation.serializer;

import com.google.common.net.MediaType;
import me.etki.grac.exception.SerializationException;
import me.etki.grac.io.SerializationResult;
import me.etki.grac.io.Serializer;
import me.etki.grac.utility.TypeSpec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class JavaNativeSerializer implements Serializer {

    public static final MediaType MIME_TYPE = MediaType.create("application", "x-java-serialized-object");

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(InputStream data, MediaType mimeType, TypeSpec type) throws IOException,
            SerializationException {

        ObjectInputStream stream = new ObjectInputStream(data);
        try {
            return (T) stream.readObject();
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Failed to find target class", e);
        } catch (StreamCorruptedException | OptionalDataException e) {
            throw new SerializationException("Stream doesn't contain valid java object serialization", e);
        }
    }

    @Override
    public <T> SerializationResult serialize(T object, MediaType mediaType) throws IOException, SerializationException {
        ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public synchronized byte[] toByteArray() {
                return buf;
            }
        };
        ObjectOutputStream stream = new ObjectOutputStream(output);
        stream.writeObject(object);
        byte[] bytes = output.toByteArray();
        return new SerializationResult()
                .setContent(new ByteArrayInputStream(bytes))
                .setSize((long) bytes.length)
                .setMimeType(MIME_TYPE);
    }

    @Override
    public boolean supports(MediaType mediaType) {
        return mediaType.equals(MIME_TYPE);
    }
}
