package me.etki.grac.infrastructure;

import me.etki.grac.transport.server.Server;
import me.etki.grac.transport.server.ServerContainer;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Servers {

    public static Server localhost() {
        return new ServerContainer(Commons.LOCALHOST);
    }

    public static Server github() {
        return new ServerContainer(Commons.GITHUB_HOST);
    }
}
