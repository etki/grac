package me.etki.grac.common;

import java.util.Objects;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ServiceAddressHelper {

    public static boolean equals(ServerDetails address, Object other) {
        if (other == address) {
            return true;
        }
        if (!(other instanceof ServerDetails)) {
            return false;
        }
        ServerDetails that = (ServerDetails) other;
        return Objects.equals(address.getProtocol(), that.getProtocol()) &&
                Objects.equals(address.getHost(), that.getHost()) && address.getPort() == that.getPort();
    }
}
