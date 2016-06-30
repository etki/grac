package me.etki.grac.transport;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Interaction {

    private TransportRequest request;
    private TransportResponse response;
    private Throwable throwable;
    private Instant startedAt;
    private Instant finishedAt;

    public TransportRequest getRequest() {
        return request;
    }

    public Interaction setRequest(TransportRequest request) {
        this.request = request;
        return this;
    }

    public TransportResponse getResponse() {
        return response;
    }

    public Interaction setResponse(TransportResponse response) {
        this.response = response;
        return this;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Interaction setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Interaction setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public Interaction setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }

    public Duration getDuration() {
        return Duration.between(startedAt, finishedAt);
    }
}
