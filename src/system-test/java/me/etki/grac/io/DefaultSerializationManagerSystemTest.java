package me.etki.grac.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import me.etki.grac.exception.NoSuitableSerializerException;
import me.etki.grac.exception.SerializationException;
import me.etki.grac.implementation.serializer.JacksonSerializer;
import me.etki.grac.implementation.serializer.JavaNativeSerializer;
import me.etki.grac.utility.TypeSpec;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Todo: use only mocks instead of real serializers, move to functional tests.
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultSerializationManagerSystemTest {

    private static final MediaType JSON_MIME_TYPE = MediaType.JSON_UTF_8;
    private static final MediaType SERIALIZED_OBJECT_MIME_TYPE = JavaNativeSerializer.MIME_TYPE;

    private final List<Serializer> serializers = Arrays.asList(
            new JacksonSerializer(new ObjectMapper()),
            new JavaNativeSerializer()
    );

    private DefaultSerializationManager serializationManager = manager(serializers);

    @Test
    public void shouldCorrectlySerializeNullPayload() throws Exception {
        SerializationResult result = serializationManager.serialize(null, JSON_MIME_TYPE);
        assertNull(result.getContent());
        assertEquals(new Long(0L), result.getSize());
    }

    @Test
    public void shouldCorrectlyDeserializeNullPayload() throws Exception {
        DeserializationResult<String> result = serializationManager
                .deserialize(null, JSON_MIME_TYPE, new TypeSpec(String.class), Collections.emptyList());
        assertFalse(result.getAltResult().isPresent());
        assertFalse(result.getResult().isPresent());
    }

    @Test
    public void shouldNotNeedSerializersToDeserializeNullPayload() throws Exception {
        Serializer serializer = mock(Serializer.class);
        when(serializer.serialize(any(), any())).thenThrow(new RuntimeException());
        when(serializer.deserialize(any(), any(), any())).thenThrow(new RuntimeException());
        when(serializer.supports(any())).thenThrow(new RuntimeException());
        DefaultSerializationManager serializationManager = manager(serializer);
        DeserializationResult<String> result = serializationManager
                .deserialize(null, JSON_MIME_TYPE, new TypeSpec(String.class), Collections.emptyList());
        assertFalse(result.getAltResult().isPresent());
        assertFalse(result.getResult().isPresent());
    }

    @Test(expected = NoSuitableSerializerException.class)
    public void shouldCorrectlyFailSerializationWithNoMatchingSerializers() throws Exception {
        serializationManager.serialize("test", MediaType.JPEG);
    }

    @Test(expected = NoSuitableSerializerException.class)
    public void shouldCorrectlyFailDeserializationWithNoMatchingSerializers() throws Exception {
        SerializationResult wrapper = serializationManager.serialize("test", JSON_MIME_TYPE);
        serializationManager.deserialize(wrapper.getContent(), MediaType.JPEG, new TypeSpec(String.class),
                Collections.emptyList());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldCorrectlyDeserializeAltResult() throws Exception {
        String value = "test";
        SerializationResult payload = serializationManager.serialize(value, JSON_MIME_TYPE);
        List<TypeSpec> fallbackTypes = Arrays.asList(new TypeSpec(Boolean.class), new TypeSpec(String.class));
        DeserializationResult<List<String>> result = serializationManager.deserialize(
                payload.getContent(),
                payload.getMimeType(),
                new TypeSpec(Integer.class),
                fallbackTypes
        );

        assertFalse(result.getResult().isPresent());
        assertTrue(result.getAltResult().isPresent());
        assertEquals(value, result.getAltResult().get());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldCorrectlyWorkWithSeveralDeserializers() throws Exception {
        String value = "test";
        List<Serializer> faultySerializers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Serializer serializer = mock(Serializer.class);
            when(serializer.supports(any())).thenReturn(true);
            when(serializer.serialize(any(), any())).thenThrow(new SerializationException());
            when(serializer.deserialize(any(), any(), any())).thenThrow(new SerializationException());
            faultySerializers.add(serializer);
        }
        byte[] fakeRepresentation = new byte[] { 1, 2, 3, 4 };
        SerializationResult fakeResult = new SerializationResult()
                .setContent(new ByteArrayInputStream(fakeRepresentation))
                .setMimeType(MediaType.ANY_TYPE)
                .setSize((long) fakeRepresentation.length);
        Serializer healthySerializer = mock(Serializer.class);
        when(healthySerializer.supports(any())).thenReturn(true);
        when(healthySerializer.serialize(any(), any())).thenReturn(fakeResult);
        when(healthySerializer.deserialize(any(), any(), any())).thenReturn(value);
        List<Serializer> serializers = new ArrayList<>(faultySerializers);
        serializers.add(healthySerializer);
        DefaultSerializationManager serializationManager = manager(serializers);
        SerializationResult result = serializationManager.serialize(value, MediaType.ANY_TYPE);
        DeserializationResult<String> deserializationResult = serializationManager.deserialize(result.getContent(),
                result.getMimeType(), new TypeSpec(String.class), Collections.emptyList());
        assertFalse(deserializationResult.getAltResult().isPresent());
        assertTrue(deserializationResult.getResult().isPresent());
        assertEquals(value, deserializationResult.getResult().get());
    }

    private static DefaultSerializationManager manager(List<Serializer> serializers) {
        return new DefaultSerializationManager(serializers, new CachingInputStreamWrapperFactory(), 32768);
    }

    private static DefaultSerializationManager manager(Serializer serializer) {
        return manager(Collections.singletonList(serializer));
    }
}