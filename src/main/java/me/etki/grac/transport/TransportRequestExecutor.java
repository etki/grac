package me.etki.grac.transport;

import com.google.common.base.Stopwatch;
import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.concurrent.TimeoutService;
import me.etki.grac.exception.NoSuitableTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class TransportRequestExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransportRequestExecutor.class);

    private final TransportRegistry transports;
    private final TimeoutService timeoutService;
    private final List<TransportInterceptor> interceptors = new ArrayList<>();

    public TransportRequestExecutor(TransportRegistry transports, TimeoutService timeoutService) {
        this.transports = transports;
        this.timeoutService = timeoutService;
    }

    public TransportRequestExecutor addInterceptor(TransportInterceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public TransportRequestExecutor removeInterceptor(TransportInterceptor interceptor) {
        interceptors.remove(interceptor);
        return this;
    }

    public CompletableFuture<ServerResponse> execute(ServerRequest request) {
        request.validate();
        Optional<Transport> container = transports.getTransport(request.getServer().getProtocol());
        if (!container.isPresent()) {
            String message = "Failed to find suitable transport for " +
                    "protocol `" + request.getServer().getProtocol() + "`";
            return CompletableFutures.exceptional(new NoSuitableTransportException(message));
        }
        Transport transport = container.get();
        LOGGER.debug("Executing request {} using transport {}", request, transport);
        return applyInterceptors(request)
                .thenCompose(transformedRequest -> {
                    CompletableFuture<ServerResponse> execution = transport.execute(transformedRequest);
                    CompletableFuture<Void> timeout = timeoutService.setTimeout(execution, request.getTimeout(),
                            TimeUnit.MILLISECONDS);
                    execution.thenRun(() -> timeout.cancel(true));
                    return execution;
                })
                .thenCompose(this::applyInterceptors);
    }

    private CompletableFuture<ServerRequest> applyInterceptors(ServerRequest request) {
        CompletableFuture<ServerRequest> synchronizer = CompletableFuture.completedFuture(request);
        if (interceptors.isEmpty()) {
            return synchronizer;
        }
        Stopwatch operationTimer = Stopwatch.createStarted();
        for (TransportInterceptor interceptor : interceptors) {
            Stopwatch timer = Stopwatch.createStarted();
            synchronizer = synchronizer
                    .thenCompose(interceptor::processRequest)
                    .thenApply(result -> {
                        LOGGER.debug("Applied interceptor {} to request {} in {}", interceptor, result, timer);
                        return result;
                    });
        }
        return synchronizer
                .thenApply(result -> {
                    LOGGER.debug("Applied {} interceptors to transport request {} in {}", interceptors.size(), request,
                            operationTimer);
                    return result;
                });

    }

    private CompletableFuture<ServerResponse> applyInterceptors(ServerResponse response) {
        CompletableFuture<ServerResponse> synchronizer = CompletableFuture.completedFuture(response);
        if (interceptors.isEmpty()) {
            return synchronizer;
        }
        Stopwatch operationTimer = Stopwatch.createStarted();
        for (TransportInterceptor interceptor : interceptors) {
            Stopwatch timer = Stopwatch.createStarted();
            synchronizer = synchronizer
                    .thenCompose(interceptor::processResponse)
                    .thenApply(result -> {
                        LOGGER.debug("Applied interceptor {} to response {} in {}", interceptor, result, timer);
                        return result;
                    });
        }
        return synchronizer
                .thenApply(result -> {
                    LOGGER.debug("Applied {} interceptors to transport response {} in {}", interceptors.size(), result,
                            operationTimer);
                    return result;
                });
    }
}
