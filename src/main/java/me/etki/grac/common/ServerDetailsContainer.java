package me.etki.grac.common;

import java.util.Objects;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ServerDetailsContainer implements ServerDetails {

    private final String protocol;
    private final String host;
    private final int port;

    public ServerDetailsContainer(String protocol, String host, int port) {
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(host);
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, host, port);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return ServerDetails.equals(this, obj);
    }

    @Override
    public String toString() {
        return "ServerDetailsContainer {protocol='" + protocol + "', host='" + host + "', port=" + port + "}";
    }
}
