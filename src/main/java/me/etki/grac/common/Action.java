package me.etki.grac.common;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public enum Action {

    READ (true),
    CREATE (false),
    SET (true),
    MODIFY (false),
    DELETE (true);

    private final boolean idempotent;

    Action(boolean idempotent) {
        this.idempotent = idempotent;
    }

    public boolean isIdempotent() {
        return idempotent;
    }
}
