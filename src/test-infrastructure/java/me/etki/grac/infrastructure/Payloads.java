package me.etki.grac.infrastructure;

import com.google.common.net.MediaType;
import me.etki.grac.transport.Payload;

import java.io.ByteArrayInputStream;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Payloads {

    public static Payload of(byte[] bytes, MediaType mimeType) {
        return new Payload()
                .setMimeType(mimeType)
                .setContent(new ByteArrayInputStream(bytes))
                .setSize((long) bytes.length);
    }

    public static Payload of(byte[] bytes) {
        return of(bytes, MediaType.OCTET_STREAM);
    }
}
