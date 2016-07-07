package me.etki.grac.io;

import com.google.common.io.ByteStreams;
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
    private final MarkResetStreamWrapperFactory wrapperFactory;
    private final int markLimit;

    public DefaultSerializationManager(List<Serializer> serializers, MarkResetStreamWrapperFactory wrapperFactory, int markLimit) {
        this.serializers = serializers;
        this.wrapperFactory = wrapperFactory;
        this.markLimit = markLimit;
    }

    public <T> SerializationResult serialize(T payload, MediaType mimeType) throws IOException, SerializationException {
        SerializationResult result = checkEarlySerializationOptions(payload, mimeType);
        if (result != null) {
            if (result.getContent() != null) {
                result.setContent(wrapperFactory.wrap(result.getContent()));
            }
            return result;
        }
        List<SerializationException> exceptionStack = new ArrayList<>();
        for (Serializer serializer : getApplicableSerializers(mimeType)) {
            try {
                result = serializer.serialize(payload, mimeType);
                if (result.getContent() != null) {
                    result.setContent(wrapperFactory.wrap(result.getContent()));
                }
                return result;
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
        stream = wrapperFactory.wrap(stream);
        // todo do not convert to bytes
        DeserializationResult<T> earlyResult = checkEarlyDeserializationOptions(stream, expectedType);
        if (earlyResult != null) {
            return earlyResult;
        }
        List<Serializer> serializers = getApplicableSerializers(mimeType);
        LOGGER.debug("Using following deserializers for mime type {}: {}", mimeType, serializers);
        List<SerializationException> exceptionStack = new ArrayList<>();
        try {
            return DeserializationResult.normal(deserializeInternal(stream, mimeType, expectedType));
        } catch (SerializationException e) {
            exceptionStack.add(e);
        }
        for (TypeSpec fallbackType : fallbackTypes) {
            try {
                return DeserializationResult.alternative(deserializeInternal(stream, mimeType, fallbackType));
            } catch (SerializationException e) {
                exceptionStack.add(e);
            }
        }
        String message = "Failed to deserialize type " + expectedType + " out of payload of mime type " + expectedType;
        throw new SerializationException(message, exceptionStack.get(0));
    }

    private <T> T deserializeInternal(InputStream stream, MediaType mimeType, TypeSpec targetType)
            throws SerializationException, IOException {

        List<Serializer> serializers = getApplicableSerializers(mimeType);
        List<SerializationException> exceptions = new ArrayList<>();
        for (Serializer serializer : serializers) {
            if (stream.markSupported()) {
                stream.mark(markLimit);
            }
            try {
                return serializer.deserialize(stream, mimeType, targetType);
            } catch (SerializationException e) {
                LOGGER.debug("Failed to deserialize stream {} to {} using serializer {}", stream, targetType,
                        serializer);
                exceptions.add(e);
                if (!tryResetStream(stream)) {
                    throw new IOException("Could not deserialize stream " + stream + " to " + targetType + ", can't " +
                            "reset stream to try other options");
                }
            }
        }
        throw exceptions.get(0);
    }

    private static boolean tryResetStream(InputStream stream) {
        LOGGER.debug("Trying to reset stream {}", stream);
        if (!stream.markSupported()) {
            LOGGER.debug("Stream doesn't support mark/reset pattern");
            return false;
        }
        try {
            stream.reset();
            LOGGER.debug("Successfully reset stream {}", stream);
            return true;
        } catch (IOException e) {
            LOGGER.debug("Failed to reset deserialized stream {} due to exception: {} {}", stream, e.getClass(),
                    e.getMessage());
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Full exception:", e);
            }
            return false;
        }
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private <T> DeserializationResult<T> checkEarlyDeserializationOptions(InputStream stream, TypeSpec expectedType)
            throws IOException {

        if (stream == null) {
            LOGGER.debug("Null payload provided, short-circuiting");
            return DeserializationResult.normal(null);
        }
        if (InputStream.class.isAssignableFrom(expectedType.getRootType())) {
            LOGGER.debug("Client code has requested raw input stream, returning it directly");
            return DeserializationResult.normal((T) stream);
        }
        if (byte[].class.isAssignableFrom(expectedType.getRootType())) {
            LOGGER.debug("Client code has requested byte array, returning it directly");
            return DeserializationResult.normal((T) ByteStreams.toByteArray(stream));
        }
        if (stream.markSupported()) {
            stream.mark(1);
            if (stream.read() == -1) {
                LOGGER.debug("Empty stream provided, short-circuiting");
                return DeserializationResult.normal(null);
            }
            stream.reset();
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
