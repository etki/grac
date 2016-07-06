package me.etki.grac.infrastructure;

import com.google.common.net.MediaType;
import me.etki.grac.io.SerializationResult;
import me.etki.grac.io.Serializer;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Serializers {

    public static Serializer mock() {
        return Mockito.mock(Serializer.class);
    }

    public static Serializer anyTypeMock() {
        Serializer mock = mock();
        when(mock.supports(any())).thenReturn(true);
        return mock;
    }

    public static SerializationResult serializationResult(byte[] bytes, MediaType mimeType) {
        return new SerializationResult()
                .setContent(bytes == null ? null : new ByteArrayInputStream(bytes))
                .setSize(bytes == null ? null : (long) bytes.length)
                .setMimeType(mimeType);
    }

    public static SerializationResult serializationResult(byte[] bytes) {
        return serializationResult(bytes, MediaType.OCTET_STREAM);
    }

    public static SerializationResult emptySerializationResult() {
        return serializationResult(new byte[0]);
    }

    public static SerializationResult nullSerializationResult() {
        return serializationResult(null);
    }
}
