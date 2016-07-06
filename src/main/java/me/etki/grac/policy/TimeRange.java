package me.etki.grac.policy;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TimeRange {

    public static final double DEFAULT_RANDOM_FACTOR = 0.2;

    private final long minimumDelay;
    private final long maximumDelay;
    private final double randomFactor;

    public TimeRange(long minimumDelay, long maximumDelay, double randomFactor) {
        this.minimumDelay = minimumDelay;
        this.maximumDelay = maximumDelay;
        this.randomFactor = randomFactor;
    }

    public TimeRange(long minimumDelay, long maximumDelay) {
        this(minimumDelay, maximumDelay, DEFAULT_RANDOM_FACTOR);
    }

    public long getMinimumDelay() {
        return minimumDelay;
    }

    public long getMaximumDelay() {
        return maximumDelay;
    }

    public double getRandomFactor() {
        return randomFactor;
    }
}
