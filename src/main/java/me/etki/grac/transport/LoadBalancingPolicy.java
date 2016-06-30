package me.etki.grac.transport;

import me.etki.grac.common.ServerDetails;

import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface LoadBalancingPolicy {

    ServerDetails getNext(List<ServerDetails> definitions);
}
