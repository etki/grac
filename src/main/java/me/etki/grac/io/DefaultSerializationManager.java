package me.etki.grac.io;

import com.google.common.net.MediaType;
import me.etki.grac.exception.NoSuitableSerializerException;
import me.etki.grac.exception.SerializationException;
import me.etki.grac.utility.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Todo quick implementation
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultSerializationManager implements SynchronousSerializationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSerializationManager.class);

    private final List<Serializer> serializers;

    public DefaultSerializationManager(List<Serializer> serializers) {
        this.serializers = serializers;
    }

    public DefaultSerializationManager(Serializer... serializers) {
        this(Arrays.asList(serializers));
    }

    public <T> SerializationResult serialize(T payload, MediaType mimeType) throws IOException, SerializationException {
        SerializationResult result = checkEarlySerializationOptions(payload, mimeType);
        if (result != null) {
            return result;
        }
        List<SerializationException> exceptionStack = new ArrayList<>();
        for (Serializer serializer : getApplicableSerializers(mimeType)) {
            try {
                return serializer.serialize(payload, mimeType);
            } catch (SerializationException e) {
                exceptionStack.add(e);
            }
        }
        String message = "Failed to serialize " + payload + " using " + serializers.size() + " serializers";
        throw new SerializationException(message, exceptionStack.get(0));
    }

    @Override
    public <T> DeserializationResult<T> deserialize(
            InputStream stream,
            MediaType mimeType,
            TypeSpec expectedType,
            List<TypeSpec> fallbackTypes)
            throws IOException, SerializationException {

        LOGGER.debug("Deserializing type {} out of payload of mime type {}", expectedType, mimeType);
        if (stream == null) {
            LOGGER.debug("Null payload provided, short-circuiting");
            return DeserializationResult.normal(null);
        }
        byte[] data = extractStream(stream);
        DeserializationResult<T> earlyResult = checkEarlyDeserializationOptions(data, expectedType);
        if (earlyResult != null) {
            return earlyResult;
        }
        ByteArrayInputStream payload = new ByteArrayInputStream(data);
        List<Serializer> serializers = getApplicableSerializers(mimeType);
        LOGGER.debug("Using following deserializers for mime type {}: {}", mimeType, serializers);
        List<SerializationException> exceptionStack = new ArrayList<>();
        for (Serializer serializer : serializers) {
            try {
                LOGGER.debug("Trying to deserialize type {} using serializer {}", expectedType, serializer);
                return DeserializationResult.normal(serializer.<T>deserialize(payload, mimeType, expectedType));
            } catch (SerializationException e) {
                LOGGER.debug("Failed to deserialize type {} using serializer {}: {}", expectedType, serializer,
                        e.getMessage());
                exceptionStack.add(e);
            } finally {
                payload.reset();
            }
        }
        for (TypeSpec spec : fallbackTypes) {
            for (Serializer serializer : serializers) {
                try {
                    LOGGER.debug("Trying to deserialize type {} using serializer {}", spec, serializer);
                    return DeserializationResult.alternative(serializer.<T>deserialize(payload, mimeType, spec));
                } catch (SerializationException e) {
                    LOGGER.debug("Failed to deserialize type {} using serializer {}: {}", spec, serializer,
                            e.getMessage());
                    exceptionStack.add(e);
                } finally {
                    payload.reset();
                }
            }
        }
        String message = "Failed to deserialize type " + expectedType + " out of payload of mime type " + expectedType;
        throw new SerializationException(message, exceptionStack.get(0));
    }

    private List<Serializer> getApplicableSerializers(MediaType mediaType) throws SerializationException {
        List<Serializer> serializers = this.serializers.stream()
                .filter(serializer -> serializer.supports(mediaType))
                .collect(Collectors.toList());
        if (serializers.isEmpty()) {
            // todo this method should not throw anything itself
            throw new NoSuitableSerializerException("Couldn't find serializer for media type " + mediaType);
        }
        return serializers;
    }

    private byte[] extractStream(InputStream stream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
            @Override
            public synchronized byte[] toByteArray() {
                return buf;
            }
        };
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private <T> DeserializationResult<T> checkEarlyDeserializationOptions(byte[] bytes, TypeSpec expectedType) {

        if (bytes.length == 0) {
            LOGGER.debug("Payload is empty, short-circuiting");
            return DeserializationResult.normal(null);
        }
        if (InputStream.class.isAssignableFrom(expectedType.getRootType())) {
            LOGGER.debug("Client code has requested raw input stream, returning it directly");
            return DeserializationResult.normal((T) new ByteArrayInputStream(bytes));
        }
        if (byte[].class.isAssignableFrom(expectedType.getRootType())) {
            LOGGER.debug("Client code has requested byte array, returning it directly");
            return DeserializationResult.normal((T) bytes);
        }
        return null;
    }

    private <T> SerializationResult checkEarlySerializationOptions(
            T payload,
            MediaType mimeType)
            throws IOException {

        LOGGER.debug("Checking early serialization options");
        if (payload == null) {
            LOGGER.debug("Null payload provided, short-circuiting");
            return new SerializationResult()
                    .setSize(0L)
                    .setContent(null)
                    .setMimeType(mimeType);
        }
        if (InputStream.class.isAssignableFrom(payload.getClass())) {
            LOGGER.debug("Input stream payload provided, short-circuiting");
            return new SerializationResult()
                    .setContent((InputStream) payload)
                    .setMimeType(mimeType);
        }
        if (payload.getClass().equals(byte[].class)) {
            LOGGER.debug("Byte array payload provided, short-circuiting");
            return new SerializationResult()
                    .setContent(new ByteArrayInputStream((byte[]) payload))
                    .setMimeType(mimeType);
        }
        LOGGER.debug("Could not shortcut serialization using early options");
        return null;
    }
}
