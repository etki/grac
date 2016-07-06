package me.etki.grac.utility;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class StaticValidator {

    public static void verifyState(boolean assertion, String message) {
        if (!assertion) {
            throw new IllegalStateException(message);
        }
    }

    public static void requireNonNull(Object value, String message) {
        verifyState(value != null, message);
    }
}
