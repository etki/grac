package me.etki.grac.transport;

import me.etki.grac.common.ServerDetails;

import java.util.List;
import java.util.Random;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class RandomHostLoadBalancingPolicy implements LoadBalancingPolicy {

    private static final Random RANDOM = new Random();

    @Override
    public ServerDetails getNext(List<ServerDetails> definitions) {
        return definitions.get(RANDOM.nextInt(definitions.size()));
    }
}
