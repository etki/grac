package me.etki.grac.policy;

import me.etki.grac.transport.server.Server;

import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface LoadBalancingPolicy {

    Server getNext(List<Server> definitions);
}
