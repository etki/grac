package me.etki.grac.transport;

import java.time.Instant;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class RequestContext {

    private TLRequest tlRequest;
    private TransportRequest transportRequest;
    private int attemptNumber;
    private Instant startedAt;
    private Instant finishedAt;
    private Trace trace;

    public TLRequest getTlRequest() {
        return tlRequest;
    }

    public RequestContext setTlRequest(TLRequest tlRequest) {
        this.tlRequest = tlRequest;
        return this;
    }

    public TransportRequest getTransportRequest() {
        return transportRequest;
    }

    public RequestContext setTransportRequest(TransportRequest transportRequest) {
        this.transportRequest = transportRequest;
        return this;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public RequestContext setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
        return this;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public RequestContext setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }

    public Trace getTrace() {
        return trace;
    }

    public RequestContext setTrace(Trace trace) {
        this.trace = trace;
        return this;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public RequestContext setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
        return this;
    }
}
