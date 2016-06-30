package me.etki.grac.common;

import com.google.common.net.MediaType;

import java.io.InputStream;

/**
 * This class represents request / response payload that may be sent or received by client.
 * It is implied that if payload is present, it always has corresponding mime-type specified, but length may be not
 * known until the very end of stream read.
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Payload {

    /**
     * May not be null
     */
    private MediaType mimeType;
    /**
     * May be null
     */
    private Long size;
    /**
     * May not be null
     */
    private InputStream content;

    public MediaType getMimeType() {
        return mimeType;
    }

    public Payload setMimeType(MediaType mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public Payload setSize(Long size) {
        this.size = size;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public Payload setContent(InputStream content) {
        this.content = content;
        return this;
    }
}
