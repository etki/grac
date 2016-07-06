package me.etki.grac.transport.server;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * Default {@link Server} implementation.
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ServerContainer implements Server {

    private final InetSocketAddress address;
    private final String protocol;

    public ServerContainer(InetSocketAddress address, String protocol) {
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(address);
        this.address = address;
        this.protocol = protocol;
    }

    public ServerContainer(Server server) {
        this(server.getAddress(), server.getProtocol());
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }

    @Override
    public int hashCode() {
        return Server.hash(this);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return Server.equals(this, obj);
    }

    @Override
    public String toString() {
        return "ServerContainer {address='" + address + ", protocol='" + protocol + "'}";
    }
}
