package me.etki.grac.utility;

import java.util.Random;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class MathUtilities {

    private static final Random RANDOM = new Random();

    public static long limit(long value, long lower, long upper) {
        if (lower > upper) {
            throw new IllegalArgumentException("Lower (" + lower + ") bound is bigger than upper (" + upper + ")");
        }
        if (lower > value) {
            return lower;
        }
        if (upper < value) {
            return upper;
        }
        return value;
    }

    public static long applyRandomFactor(long value, double randomFactor) {
        if (randomFactor == 0.0) {
            return value;
        }
        randomFactor = randomFactor * RANDOM.nextDouble();
        double multiplier = 1.0 + (RANDOM.nextBoolean() ? randomFactor : -randomFactor);
        return (long) (value * multiplier);
    }
}
