package me.etki.grac.transport.server;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface Server {

    InetSocketAddress getAddress();
    String getProtocol();

    default String getName() {
        return getAddress().getHostString();
    }

    static boolean equals(Server server, Object other) {
        if (other == server) {
            return true;
        }
        if (!(other instanceof Server)) {
            return false;
        }
        Server that = (Server) other;
        return Objects.equals(server.getProtocol(), that.getProtocol()) &&
                Objects.equals(server.getAddress(), that.getAddress());
    }

    static int hash(Server server) {
        return Objects.hash(server.getAddress(), server.getProtocol());
    }
}
