package me.etki.grac.io;

import com.google.common.io.ByteStreams;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class CachingInputStreamWrapperUnitTest {

    private static final byte[] TEST_SOURCE = new byte[] { 1, 2, 3, 4 };

    private ByteArrayInputStream sourceStream;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        sourceStream = new ByteArrayInputStream(TEST_SOURCE);
    }

    @Test
    public void shouldSuccessfullyBufferize() throws Exception {
        CachingInputStreamWrapper wrapper = new CachingInputStreamWrapper(sourceStream);
        wrapper.mark(TEST_SOURCE.length);
        assertArrayEquals(TEST_SOURCE, ByteStreams.toByteArray(wrapper));
        wrapper.reset();
        assertArrayEquals(TEST_SOURCE, ByteStreams.toByteArray(wrapper));
    }

    @Test
    public void shouldFailToBufferizeContentOverLimit() throws Exception {
        CachingInputStreamWrapper wrapper = new CachingInputStreamWrapper(sourceStream);
        wrapper.mark(TEST_SOURCE.length - 1);
        byte[] firstDump = ByteStreams.toByteArray(wrapper);
        assertArrayEquals(TEST_SOURCE, firstDump);

        expectedException.expect(IOException.class);
        wrapper.reset();
    }

    @Test
    public void shouldCorrectlyHandleRemarkDuringReread() throws Exception {
        CachingInputStreamWrapper wrapper = new CachingInputStreamWrapper(sourceStream);
        wrapper.mark(TEST_SOURCE.length);
        int length = TEST_SOURCE.length / 2;
        ByteStreams.toByteArray(wrapper);
        wrapper.reset();
        wrapper.mark(length);
        assertArrayEquals(Arrays.copyOf(TEST_SOURCE, length), ByteStreams.toByteArray(wrapper));
    }
}
