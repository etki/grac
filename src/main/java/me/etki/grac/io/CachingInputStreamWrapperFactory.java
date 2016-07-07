package me.etki.grac.io;

import java.io.InputStream;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class CachingInputStreamWrapperFactory implements MarkResetStreamWrapperFactory {

    @Override
    public InputStream wrap(InputStream source) {
        return new CachingInputStreamWrapper(source);
    }
}
