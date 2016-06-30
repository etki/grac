package me.etki.grac.transport;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Trace {

    private final List<Interaction> interactions = new ArrayList<>();
    private Instant startedAt;
    private Instant finishedAt;

    public Trace addInteraction(Interaction interaction) {
        interactions.add(interaction);
        return this;
    }

    public List<Interaction> getInteractions() {
        return interactions;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Trace start() {
        startedAt = startedAt == null ? Instant.now() : startedAt;
        return this;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public Trace finish() {
        finishedAt = finishedAt == null ? Instant.now() : finishedAt;
        return this;
    }

    public Trace setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public Trace setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }

    public Duration getDuration() {
        if (startedAt == null || finishedAt == null) {
            return null;
        }
        return Duration.between(startedAt, finishedAt);
    }
}
