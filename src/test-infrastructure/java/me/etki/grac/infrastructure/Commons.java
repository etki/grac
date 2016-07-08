package me.etki.grac.infrastructure;

import com.google.common.net.MediaType;
import me.etki.grac.implementation.serializer.JavaNativeSerializer;
import me.etki.grac.transport.server.Server;
import me.etki.grac.transport.server.ServerContainer;

import java.net.InetSocketAddress;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Commons {

    public static final Server LOCALHOST = new ServerContainer(new InetSocketAddress("localhost", 80), "http");
    public static final Server GITHUB_HOST = new ServerContainer(new InetSocketAddress("api.github.com", 443), "https");

    public static final MediaType DEFAULT_MIME_TYPE = JavaNativeSerializer.MIME_TYPE;
}
