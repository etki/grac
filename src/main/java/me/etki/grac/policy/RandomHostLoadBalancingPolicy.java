package me.etki.grac.policy;

import me.etki.grac.transport.server.Server;

import java.util.List;
import java.util.Random;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class RandomHostLoadBalancingPolicy implements LoadBalancingPolicy {

    public static final RandomHostLoadBalancingPolicy INSTANCE = new RandomHostLoadBalancingPolicy();

    private static final Random RANDOM = new Random();

    @Override
    public Server getNext(List<Server> definitions) {
        return definitions.isEmpty() ? null : definitions.get(RANDOM.nextInt(definitions.size()));
    }

    @Override
    public String toString() {
        return "RandomHostLoadBalancingPolicy";
    }
}
