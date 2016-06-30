package me.etki.grac.io;

import com.google.common.net.MediaType;

import java.io.InputStream;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class SerializationResult {

    private MediaType mimeType;
    private InputStream content;
    private Long size;

    public MediaType getMimeType() {
        return mimeType;
    }

    public SerializationResult setMimeType(MediaType mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public SerializationResult setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public SerializationResult setSize(Long size) {
        this.size = size;
        return this;
    }
}
