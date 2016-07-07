package me.etki.grac.io;

import com.google.common.io.ByteStreams;
import com.google.common.net.MediaType;
import me.etki.grac.infrastructure.Serializers;
import me.etki.grac.utility.TypeSpec;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class DefaultSerializationManagerFunctionalTest {

    private static final TypeSpec INTEGER_TYPE = new TypeSpec(Integer.class);
    private static final TypeSpec INPUT_STREAM_TYPE = new TypeSpec(InputStream.class);
    private static final TypeSpec BYTE_ARRAY_TYPE = new TypeSpec(byte[].class);

    public static final byte[] EMPTY_STREAM_SOURCE = new byte[0];
    public static final byte[] NORMAL_STREAM_SOURCE = new byte[] { 1, 2, 3, 4 };
    public static final ByteArrayInputStream EMPTY_STREAM = new ByteArrayInputStream(EMPTY_STREAM_SOURCE);
    public static final ByteArrayInputStream NORMAL_STREAM = new ByteArrayInputStream(NORMAL_STREAM_SOURCE);

    private Serializer serializerMock;

    @Before
    public void setUp() throws Exception {
        EMPTY_STREAM.reset();
        NORMAL_STREAM.reset();
        serializerMock = serializerMock();
    }

    @Test
    public void shouldNotCallSerializersOnByteArraySerialization() throws Exception {
        Serializer serializer = serializerMock();

        SerializationResult result = manager(serializer)
                .serialize(NORMAL_STREAM_SOURCE, MediaType.OCTET_STREAM);

        assertNotNull(result.getContent());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteStreams.copy(result.getContent(), outputStream);
        assertArrayEquals(NORMAL_STREAM_SOURCE, outputStream.toByteArray());

        verify(serializer, times(0)).serialize(any(), any());
        verify(serializer, times(0)).deserialize(any(), any(), any());
    }

    @Test
    public void shouldNotCallSerializersOnDeserializationToByteArray() throws Exception {
        Serializer serializer = serializerMock();

        DeserializationResult<byte[]> result = manager(serializer)
                .deserialize(NORMAL_STREAM, MediaType.OCTET_STREAM, BYTE_ARRAY_TYPE);

        assertTrue(result.getResult().isPresent());
        assertArrayEquals(NORMAL_STREAM_SOURCE, result.getResult().get());

        verify(serializer, times(0)).serialize(any(), any());
        verify(serializer, times(0)).deserialize(any(), any(), any());
    }

    @Test
    public void shouldNotCallSerializersOnInputStreamSerialization() throws Exception {
        Serializer serializer = serializerMock();

        SerializationResult result = manager(serializer)
                .serialize(NORMAL_STREAM, MediaType.OCTET_STREAM);

        assertNotNull(result.getContent());
        // not verifying stream equality since it should be wrapped with caching wrapper
        assertArrayEquals(NORMAL_STREAM_SOURCE, ByteStreams.toByteArray(result.getContent()));

        verify(serializer, times(0)).serialize(any(), any());
        verify(serializer, times(0)).deserialize(any(), any(), any());
    }

    @Test
    public void shouldNotCallSerializersOnDeserializationToInputStream() throws Exception {
        Serializer serializer = serializerMock();

        DeserializationResult<InputStream> result = manager(serializer)
                .deserialize(NORMAL_STREAM, MediaType.OCTET_STREAM, INPUT_STREAM_TYPE);
        assertTrue(result.getResult().isPresent());
        // not verifying stream equality since it should be wrapped with caching wrapper
        assertArrayEquals(NORMAL_STREAM_SOURCE, ByteStreams.toByteArray(result.getResult().get()));

        verify(serializer, times(0)).serialize(any(), any());
        verify(serializer, times(0)).deserialize(any(), any(), any());
    }

    @Test
    public void shouldNotCallSerializersOnNullSerialization() throws Exception {
        Serializer serializer = serializerMock();

        SerializationResult result = manager(serializer)
                .serialize(null, MediaType.OCTET_STREAM);
        assertNull(result.getContent());

        verify(serializer, times(0)).serialize(any(), any());
        verify(serializer, times(0)).deserialize(any(), any(), any());
    }

    @Test
    public void shouldNotCallSerializersOnNullDeserialization() throws Exception {
        Serializer serializer = serializerMock();

        DeserializationResult<Integer> result = manager(serializer)
                .deserialize(null, MediaType.OCTET_STREAM, INTEGER_TYPE);
        assertFalse(result.getResult().isPresent());
        assertFalse(result.getAltResult().isPresent());

        verify(serializer, times(0)).serialize(any(), any());
        verify(serializer, times(0)).deserialize(any(), any(), any());
    }

    @Test
    public void shouldNotCallSerializersOnEmptyStreamDeserialization() throws Exception {
        Serializer serializer = serializerMock();

        DeserializationResult<Integer> result = manager(serializer)
                .deserialize(EMPTY_STREAM, MediaType.OCTET_STREAM, INTEGER_TYPE);
        assertFalse(result.getResult().isPresent());
        assertFalse(result.getAltResult().isPresent());

        verify(serializer, times(0)).serialize(any(), any());
        verify(serializer, times(0)).deserialize(any(), any(), any());
    }

    private static Serializer serializerMock() throws Exception {
        Serializer serializer = Serializers.anyTypeMock();
        when(serializer.serialize(any(), any())).thenReturn(null);
        when(serializer.deserialize(any(), any(), any())).thenReturn(null);
        return serializer;
    }

    private static DefaultSerializationManager manager(Serializer serializer) {
        List<Serializer> serializers = Collections.singletonList(serializer);
        return new DefaultSerializationManager(serializers, new CachingInputStreamWrapperFactory(), 32768);
    }
}
