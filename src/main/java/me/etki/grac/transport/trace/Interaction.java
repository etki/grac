package me.etki.grac.transport.trace;

import me.etki.grac.transport.ServerRequest;
import me.etki.grac.transport.ServerResponse;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Interaction {

    private ServerRequest request;
    private ServerResponse response;
    private Throwable throwable;
    private Instant startedAt;
    private Instant finishedAt;

    public Interaction() {
    }

    public Interaction(ServerRequest request, ServerResponse response, Throwable throwable, Instant startedAt, Instant finishedAt) {
        this.request = request;
        this.response = response;
        this.throwable = throwable;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public ServerRequest getRequest() {
        return request;
    }

    public Interaction setRequest(ServerRequest request) {
        this.request = request;
        return this;
    }

    public ServerResponse getResponse() {
        return response;
    }

    public Interaction setResponse(ServerResponse response) {
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
