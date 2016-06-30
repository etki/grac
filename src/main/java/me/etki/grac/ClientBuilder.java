package me.etki.grac;

import com.google.common.net.MediaType;
import me.etki.grac.common.ServerAddressProvider;
import me.etki.grac.concurrent.ScheduledExecutor;
import me.etki.grac.concurrent.ScheduledHelper;
import me.etki.grac.io.DefaultSerializationManager;
import me.etki.grac.io.Serializer;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.transport.DefaultTransportManager;
import me.etki.grac.transport.LoadBalancingPolicy;
import me.etki.grac.transport.ServerRegistry;
import me.etki.grac.transport.Transport;
import me.etki.grac.transport.TransportInterceptor;
import me.etki.grac.transport.TransportRegistry;
import me.etki.grac.utility.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ClientBuilder {

    private List<Supplier<Serializer>> serializers = new ArrayList<>();
    private List<Supplier<Transport>> transports = new ArrayList<>();
    private List<Supplier<Interceptor>> interceptors = new ArrayList<>();
    private List<Supplier<TransportInterceptor>> transportInterceptors = new ArrayList<>();
    private Supplier<ScheduledExecutorService> scheduler = SharedDefaults::getDefaultScheduler;
    private Supplier<Executor> responseProcessingExecutor = SharedDefaults::getDefaultExecutor;
    private ServerAddressProvider serverAddressProvider;
    private RetryPolicy retryPolicy = SharedDefaults.DEFAULT_RETRY_POLICY;
    private LoadBalancingPolicy loadBalancingPolicy = SharedDefaults.DEFAULT_LOAD_BALANCING_POLICY;
    private MediaType defaultSerializationType = SharedDefaults.JSON_MIME_TYPE;
    private List<MediaType> defaultAcceptedTypes = new ArrayList<>();
    private List<TypeSpec> fallbackTypes = new ArrayList<>();
    private List<String> acceptedLocales = new ArrayList<>();
    private boolean throwOnClientError = true;
    private boolean throwOnServerError = true;
    private boolean throwOnInvalidResponsePayloadType = true;
    private long defaultTimeout = SharedDefaults.DEFAULT_REQUEST_TIMEOUT;
    private String clientIdentifier;

    public ClientBuilder() {
        withDefaults();
    }

    public ClientBuilder withDefaults() {
        return this;
    }

    public ClientBuilder withSerializer(Serializer serializer) {
        return withSerializer(() -> serializer);
    }

    public ClientBuilder withSerializer(Supplier<Serializer> serializer) {
        serializers.add(serializer);
        return this;
    }

    public ClientBuilder withSerializers(Iterable<Serializer> serializers) {
        serializers.forEach(this::withSerializer);
        return this;
    }

    public ClientBuilder setSerializers(Iterable<Serializer> serializers) {
        this.serializers = StreamSupport.stream(serializers.spliterator(), false)
                .map(serializer -> (Supplier<Serializer>) () -> serializer)
                .collect(Collectors.toList());
        return this;
    }

    public ClientBuilder withoutSerializers() {
        serializers = new ArrayList<>();
        return this;
    }

    public ClientBuilder withTransport(Transport transport) {
        return withTransport(() -> transport);
    }

    public ClientBuilder withTransport(Supplier<Transport> transport) {
        transports.add(transport);
        return this;
    }

    public ClientBuilder withTransports(Iterable<Transport> transports) {
        transports.forEach(this::withTransport);
        return this;
    }

    public ClientBuilder setTransports(Iterable<Transport> transports) {
        this.transports = StreamSupport.stream(transports.spliterator(), false)
                .map(transport -> (Supplier<Transport>) () -> transport)
                .collect(Collectors.toList());
        return this;
    }

    public ClientBuilder withoutTransports() {
        transports = new ArrayList<>();
        return this;
    }

    public ClientBuilder withInterceptor(Interceptor interceptor) {
        return withInterceptor(() -> interceptor);
    }

    public ClientBuilder withInterceptor(Supplier<Interceptor> interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public ClientBuilder withInterceptors(Iterable<Interceptor> interceptors) {
        interceptors.forEach(this::withInterceptor);
        return this;
    }

    public ClientBuilder setInterceptors(Iterable<Interceptor> interceptors) {
        this.interceptors = StreamSupport.stream(interceptors.spliterator(), false)
                .map(interceptor -> (Supplier<Interceptor>) () -> interceptor)
                .collect(Collectors.toList());
        return this;
    }

    public ClientBuilder withoutInterceptors() {
        interceptors = new ArrayList<>();
        return this;
    }

    public ClientBuilder withTransportInterceptor(TransportInterceptor interceptor) {
        return withTransportInterceptor(() -> interceptor);
    }

    public ClientBuilder withTransportInterceptor(Supplier<TransportInterceptor> interceptor) {
        transportInterceptors.add(interceptor);
        return this;
    }

    public ClientBuilder withTransportInterceptors(Iterable<TransportInterceptor> interceptors) {
        interceptors.forEach(this::withTransportInterceptor);
        return this;
    }

    public ClientBuilder setTransportInterceptors(Iterable<TransportInterceptor> interceptors) {
        transportInterceptors = StreamSupport
                .stream(interceptors.spliterator(), false)
                .map(interceptor -> (Supplier<TransportInterceptor>) () -> interceptor)
                .collect(Collectors.toList());
        return this;
    }

    public ClientBuilder withoutTransportInterceptors() {
        transportInterceptors = new ArrayList<>();
        return this;
    }

    public ClientBuilder withScheduler(ScheduledExecutorService scheduler) {
        return withScheduler(() -> scheduler);
    }

    public ClientBuilder withScheduler(Supplier<ScheduledExecutorService> scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public ClientBuilder withResponseProcessingExecutor(Executor executor) {
        return withResponseProcessingExecutor(() -> executor);
    }

    public ClientBuilder withResponseProcessingExecutor(Supplier<Executor> executor) {
        responseProcessingExecutor = executor;
        return this;
    }

    public ClientBuilder withRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public ClientBuilder withServiceAddressProvider(ServerAddressProvider provider) {
        serverAddressProvider = provider;
        return this;
    }

    public ClientBuilder withDefaultAcceptedType(MediaType acceptedType) {
        defaultAcceptedTypes.add(acceptedType);
        return this;
    }

    public ClientBuilder withDefaultAcceptedTypes(Iterable<MediaType> acceptedTypes) {
        acceptedTypes.forEach(defaultAcceptedTypes::add);
        return this;
    }

    public ClientBuilder withoutDefaultAcceptedTypes() {
        defaultAcceptedTypes = new ArrayList<>();
        return this;
    }

    public ClientBuilder withDefaultSerializationType(MediaType serializationType) {
        defaultSerializationType = serializationType;
        return this;
    }
    
    public ClientBuilder withFallbackType(TypeSpec type) {
        fallbackTypes.add(type);
        return this;
    }
    
    public ClientBuilder withFallbackTypes(Iterable<TypeSpec> types) {
        types.forEach(type -> {
            if (!fallbackTypes.contains(type)) {
                fallbackTypes.add(type);
            }
        });
        return this;
    }
    
    public ClientBuilder setFallbackTypes(Iterable<TypeSpec> types) {
        fallbackTypes = StreamSupport.stream(types.spliterator(), false).collect(Collectors.toList());
        return this;
    }
    
    public ClientBuilder withoutFallbackTypes() {
        fallbackTypes = new ArrayList<>();
        return this;
    }

    public ClientBuilder withAcceptedLocale(String locale) {
        acceptedLocales.add(locale);
        return this;
    }

    public ClientBuilder withAcceptedLocales(Iterable<String> locales) {
        locales.forEach(acceptedLocales::add);
        return this;
    }

    public ClientBuilder setAcceptedLocales(Iterable<String> locales) {
        this.acceptedLocales = StreamSupport.stream(locales.spliterator(), false).collect(Collectors.toList());
        return this;
    }

    public ClientBuilder withoutAcceptedLocales() {
        this.acceptedLocales = new ArrayList<>();
        return this;
    }
    
    public ClientBuilder shouldThrowOnClientError(boolean throwOnClientError) {
        this.throwOnClientError = throwOnClientError;
        return this;
    }

    public ClientBuilder shouldThrowOnServerError(boolean throwOnServerError) {
        this.throwOnServerError = throwOnServerError;
        return this;
    }

    public ClientBuilder shouldThrowOnInvalidResponsePayloadType(boolean throwOnInvalidResponsePayloadType) {
        this.throwOnInvalidResponsePayloadType = throwOnInvalidResponsePayloadType;
        return this;
    }

    public ClientBuilder withClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
        return this;
    }

    public ClientBuilder withoutClientIdentifier() {
        clientIdentifier = null;
        return this;
    }

    public ClientBuilder withDefaultTimeout(long defaultTimeout) {
        if (defaultTimeout > 1) {
            throw new IllegalArgumentException("Timeout can't be less than 1");
        }
        this.defaultTimeout = defaultTimeout;
        return this;
    }

    public Client build() {
        List<Transport> transports = this.transports.stream().map(Supplier::get).collect(Collectors.toList());
        List<Serializer> serializers = this.serializers.stream().map(Supplier::get).collect(Collectors.toList());
        List<Interceptor> interceptors = this.interceptors.stream().map(Supplier::get).collect(Collectors.toList());
        ScheduledExecutorService scheduledExecutorService = this.scheduler.get();
        ScheduledExecutor scheduledExecutor = new ScheduledExecutor(scheduledExecutorService);
        ScheduledHelper scheduler = new ScheduledHelper(scheduledExecutor);
        // todo check invariants

        TransportRegistry transportRegistry = new TransportRegistry(transports);
        ServerRegistry servers = new ServerRegistry(serverAddressProvider, transportRegistry, loadBalancingPolicy);
        DefaultTransportManager transportManager = new DefaultTransportManager(transportRegistry, servers, scheduler);
        DefaultSerializationManager serializationManager
                = new DefaultSerializationManager(serializers);
        RequestOptions defaultRequestOptions = new RequestOptions()
                .setRetryPolicy(retryPolicy)
                .setSerializationType(defaultSerializationType)
                .setAcceptedLocales(acceptedLocales)
                .setAcceptedTypes(defaultAcceptedTypes)
                .setFallbackTypes(fallbackTypes)
                .setTimeout(defaultTimeout);
        ResponseProcessingOptions responseProcessingOptions = new ResponseProcessingOptions()
                .setThrowOnClientError(throwOnClientError)
                .setThrowOnServerError(throwOnServerError)
                .setThrowOnInvalidResponsePayloadType(throwOnInvalidResponsePayloadType);

        RequestResponseProcessor processor = new RequestResponseProcessor(interceptors, responseProcessingOptions);

        ClientOptions clientOptions = new ClientOptions()
                .setDefaultSerializationType(defaultSerializationType);
        return new DefaultClient(transportManager, serializationManager, processor, defaultRequestOptions,
                clientOptions);
    }
}
