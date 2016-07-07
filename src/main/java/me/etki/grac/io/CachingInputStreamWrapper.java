package me.etki.grac.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * todo current implementation is highly unoptimized
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class CachingInputStreamWrapper extends InputStream {

    private final InputStream source;

    private volatile Buffer buffer;

    public CachingInputStreamWrapper(InputStream source) {
        Objects.requireNonNull(source);
        this.source = source;
    }

    @Override
    public synchronized int read() throws IOException {
        if (buffer == null) {
            return source.read();
        }
        int value = buffer.read();
        if (value != -1) {
            return value;
        }
        value = source.read();
        if (value != -1) {
            if (!buffer.append((byte) value)) {
                buffer = null;
            }
        }
        return value;
    }

    @Override
    public synchronized void mark(int readlimit) {
        Buffer current = buffer;
        Buffer next = new Buffer(readlimit);
        if (current != null) {
            int value;
            while ((value = current.read()) != -1) {
                next.append(value);
            }
            next.reset();
        }
        buffer = next;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public synchronized void reset() throws IOException {
        if (buffer == null) {
            throw new IOException("Mark has not been called or buffer has exploded");
        }
        buffer.reset();
    }

    private static class Buffer {

        private final int limit;
        private final ArrayList<Byte> buffer;

        private final AtomicInteger cursor = new AtomicInteger(0);

        public Buffer(int limit) {
            this.limit = limit;
            buffer = new ArrayList<>(Math.max(1024, limit));
        }

        public synchronized int read() {
            int index = cursor.get();
            if (buffer.size() <= cursor.get()) {
                return -1;
            }
            cursor.incrementAndGet();
            return buffer.get(index).intValue();
        }

        public synchronized boolean append(int value) {
            if (buffer.size() >= limit) {
                return false;
            }
            buffer.add((byte) value);
            cursor.incrementAndGet();
            return true;
        }

        public synchronized void reset() {
            cursor.set(0);
        }

        public synchronized void flush() {
            buffer.clear();
            buffer.trimToSize();
            cursor.set(0);
        }
    }
}
