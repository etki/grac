package me.etki.grac;

import com.google.common.base.Stopwatch;
import com.google.common.net.MediaType;
import me.etki.grac.application.ApplicationClient;
import me.etki.grac.application.ApplicationLevelInterceptor;
import me.etki.grac.common.SharedDefaults;
import me.etki.grac.concurrent.BasicCompletableFutureFactory;
import me.etki.grac.concurrent.CompletableFutureFactory;
import me.etki.grac.concurrent.DefaultDelayService;
import me.etki.grac.concurrent.DefaultScheduledExecutor;
import me.etki.grac.concurrent.DefaultTimeoutService;
import me.etki.grac.concurrent.DelayService;
import me.etki.grac.concurrent.ScheduledExecutor;
import me.etki.grac.concurrent.TimeoutService;
import me.etki.grac.io.CachingInputStreamWrapperFactory;
import me.etki.grac.io.DefaultSerializationManager;
import me.etki.grac.io.MarkResetStreamWrapperFactory;
import me.etki.grac.io.Serializer;
import me.etki.grac.io.SynchronousSerializationManager;
import me.etki.grac.policy.LoadBalancingPolicy;
import me.etki.grac.policy.RetryPolicy;
import me.etki.grac.transport.DefaultTransportManager;
import me.etki.grac.transport.Transport;
import me.etki.grac.transport.TransportInterceptor;
import me.etki.grac.transport.TransportManager;
import me.etki.grac.transport.TransportRegistry;
import me.etki.grac.transport.TransportRequestExecutor;
import me.etki.grac.transport.server.DefaultServerRegistry;
import me.etki.grac.transport.server.Server;
import me.etki.grac.transport.server.ServerProvider;
import me.etki.grac.transport.server.ServerRegistry;
import me.etki.grac.utility.StaticValidator;
import me.etki.grac.utility.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientBuilder.class);

    private List<Supplier<Serializer>> serializers = new ArrayList<>();
    private List<Supplier<Transport>> transports = new ArrayList<>();
    private List<Supplier<ApplicationLevelInterceptor>> interceptors = new ArrayList<>();
    private List<Supplier<TransportInterceptor>> transportInterceptors = new ArrayList<>();
    private Supplier<ScheduledExecutor> scheduler = SharedDefaults::getDefaultScheduler;
    private Supplier<Executor> responseProcessingExecutor = SharedDefaults::getDefaultExecutor;
    private ServerProvider serverProvider;
    // todo: add possibility to set up fixed server provider just by specifying list of servers.
    private List<Server> servers = new ArrayList<>();
    private RetryPolicy retryPolicy = SharedDefaults.DEFAULT_RETRY_POLICY;
    private LoadBalancingPolicy loadBalancingPolicy = SharedDefaults.DEFAULT_LOAD_BALANCING_POLICY;
    private MediaType defaultSerializationType = SharedDefaults.JSON_MIME_TYPE;
    // todo: add default content-type so servers wouldn't necessarily have to specify it
    private MediaType defaultIncomingMimeType = SharedDefaults.JSON_MIME_TYPE;
    private List<MediaType> acceptedMimeTypes = new ArrayList<>();
    private List<TypeSpec> fallbackObjectTypes = new ArrayList<>();
    private List<String> acceptedLocales = new ArrayList<>();
    private boolean throwOnClientError = true;
    private boolean throwOnServerError = true;
    private boolean throwOnInvalidResponsePayloadType = true;
    private long defaultTimeout = SharedDefaults.DEFAULT_REQUEST_TIMEOUT;
    private String clientIdentifier = SharedDefaults.DEFAULT_CLIENT_IDENTIFIER;
    private Supplier<MarkResetStreamWrapperFactory> markResetStreamWrapperFactory
            = CachingInputStreamWrapperFactory::new;
    private int inputStreamMarkLimit = SharedDefaults.DEFAULT_INPUT_STREAM_MARK_LIMIT;

    // todo add upper bounds on submitted requests and concurrently executed requests
    private int requestQueueSize = SharedDefaults.DEFAULT_REQUEST_QUEUE_SIZE;
    private int concurrentRequestLimit = SharedDefaults.DEFAULT_CONCURRENT_REQUEST_LIMIT;

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

    public ClientBuilder withInterceptor(ApplicationLevelInterceptor interceptor) {
        return withInterceptor(() -> interceptor);
    }

    public ClientBuilder withInterceptor(Supplier<ApplicationLevelInterceptor> interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public ClientBuilder withInterceptors(Iterable<ApplicationLevelInterceptor> interceptors) {
        interceptors.forEach(this::withInterceptor);
        return this;
    }

    public ClientBuilder setInterceptors(Iterable<ApplicationLevelInterceptor> interceptors) {
        this.interceptors = StreamSupport.stream(interceptors.spliterator(), false)
                .map(interceptor -> (Supplier<ApplicationLevelInterceptor>) () -> interceptor)
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

    public ClientBuilder withSchedulerService(ScheduledExecutorService scheduler) {
        return withSchedulerService(() -> scheduler);
    }

    public ClientBuilder withSchedulerService(Supplier<ScheduledExecutorService> scheduler) {
        return withScheduler(() -> new DefaultScheduledExecutor(scheduler.get()));
    }

    public ClientBuilder withScheduler(ScheduledExecutor scheduler) {
        return withScheduler(() -> scheduler);
    }

    public ClientBuilder withScheduler(Supplier<ScheduledExecutor> scheduler) {
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

    public ClientBuilder withServiceAddressProvider(ServerProvider provider) {
        serverProvider = provider;
        return this;
    }

    public ClientBuilder withDefaultAcceptedType(MediaType acceptedType) {
        acceptedMimeTypes.add(acceptedType);
        return this;
    }

    public ClientBuilder withDefaultAcceptedTypes(Iterable<MediaType> acceptedTypes) {
        acceptedTypes.forEach(this.acceptedMimeTypes::add);
        return this;
    }

    public ClientBuilder withoutDefaultAcceptedTypes() {
        acceptedMimeTypes = new ArrayList<>();
        return this;
    }

    public ClientBuilder withDefaultSerializationType(MediaType serializationType) {
        defaultSerializationType = serializationType;
        return this;
    }

    public ClientBuilder withoutDefaultSerializationType() {
        return withDefaultSerializationType(null);
    }
    
    public ClientBuilder withFallbackType(TypeSpec type) {
        fallbackObjectTypes.add(type);
        return this;
    }
    
    public ClientBuilder withFallbackTypes(Iterable<TypeSpec> types) {
        types.forEach(type -> {
            if (!fallbackObjectTypes.contains(type)) {
                fallbackObjectTypes.add(type);
            }
        });
        return this;
    }
    
    public ClientBuilder setFallbackObjectTypes(Iterable<TypeSpec> types) {
        fallbackObjectTypes = StreamSupport.stream(types.spliterator(), false).collect(Collectors.toList());
        return this;
    }
    
    public ClientBuilder withoutFallbackTypes() {
        fallbackObjectTypes = new ArrayList<>();
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
        if (defaultTimeout < 1) {
            throw new IllegalArgumentException("Timeout can't be less than 1");
        }
        this.defaultTimeout = defaultTimeout;
        return this;
    }

    public ClientBuilder withMarkResetStreamWrapperFactory(Supplier<MarkResetStreamWrapperFactory> factory) {
        Objects.requireNonNull(factory);
        markResetStreamWrapperFactory = factory;
        return this;
    }

    public ClientBuilder withMarkResetStreamWrapperFactory(MarkResetStreamWrapperFactory factory) {
        return withMarkResetStreamWrapperFactory(() -> factory);
    }

    public Client build() {
        LOGGER.debug("Building client");
        Stopwatch timer = Stopwatch.createStarted();
        // poor man's DI and validation
        // todo make sure options are calculated before real work - and don't forget JMM is watching you
        Client client = new DefaultClient(constructApplicationClient(), calculateRequestOptions(),
                calculateClientOptions());
        LOGGER.debug("Built generic rest api client in {}", timer);
        return client;
    }

    private ClientOptions calculateClientOptions() {
        return new ClientOptions()
                .setThrowOnClientError(throwOnClientError)
                .setThrowOnServerError(throwOnServerError)
                .setThrowOnInvalidResponsePayloadType(throwOnInvalidResponsePayloadType);
    }

    private RequestOptions calculateRequestOptions() {
        StaticValidator.requireNonNull(defaultTimeout, "Default timeout not set");
        StaticValidator.requireNonNull(retryPolicy, "Default retry policy not set");
        StaticValidator.requireNonNull(defaultSerializationType, "Default serialization type not set");
        List<String> acceptedLocales = Optional.ofNullable(this.acceptedLocales).orElseGet(ArrayList::new);
        List<MediaType> acceptedMimeTypes = Optional.ofNullable(this.acceptedMimeTypes).orElseGet(ArrayList::new);
        List<TypeSpec> fallbackObjectTypes = Optional.ofNullable(this.fallbackObjectTypes).orElseGet(ArrayList::new);
        if (acceptedLocales.isEmpty()) {
            LOGGER.warn("No default accepted locales set, this may not be desired");
        }
        if (acceptedMimeTypes.isEmpty()) {
            LOGGER.warn("No default accepted mime types set, this may leave target server without a hint in which " +
                    "mime type data should be returned");
        }
        return new RequestOptions()
                .setRetryPolicy(retryPolicy)
                .setSerializationType(defaultSerializationType)
                .setAcceptedLocales(acceptedLocales)
                .setAcceptedMimeTypes(acceptedMimeTypes)
                .setFallbackObjectTypes(fallbackObjectTypes)
                .setTimeout(defaultTimeout)
                .setClientIdentifier(clientIdentifier);
    }

    private ApplicationClient constructApplicationClient() {
        CompletableFutureFactory responseProcessingFutureFactory = constructResponseProcessingFutureFactory();
        SynchronousSerializationManager serializationManager = constructSerializationManager();
        TransportManager transportManager = constructTransportManager();
        ApplicationClient applicationClient
                = new ApplicationClient(transportManager, serializationManager, responseProcessingFutureFactory);
        interceptors.stream().map(Supplier::get).forEach(applicationClient::addInterceptor);
        return applicationClient;
    }

    private CompletableFutureFactory constructResponseProcessingFutureFactory() {
        StaticValidator.requireNonNull(responseProcessingExecutor,
                "Executor for processing responses is not specified");
        Executor executor = responseProcessingExecutor.get();
        StaticValidator.requireNonNull(executor, "Executor for processing responses is not specified");
        return new BasicCompletableFutureFactory(executor);
    }

    private SynchronousSerializationManager constructSerializationManager() {
        MarkResetStreamWrapperFactory factory = markResetStreamWrapperFactory.get();
        if (factory == null) {
            LOGGER.warn("No mark/reset stream wrapper factory specified; that means payload-based requests won't be " +
                    "retried, and multi-variant deserialization won't be possible");
            factory = stream -> stream;
        }
        List<Serializer> serializers = this.serializers.stream()
                .map(Supplier::get)
                .filter(serializer -> {
                    if (serializer == null) {
                        LOGGER.warn("Null supplied instead of serializer");
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        if (serializers.isEmpty()) {
            LOGGER.warn("No serializers supplied. That wont necessarily lead to errors (e.g. you can safely use " +
                    "byte arrays and input streams as target type), but highly undesirable and probably " +
                    "indicates an error.");
        }
        return new DefaultSerializationManager(serializers, factory, inputStreamMarkLimit);
    }

    private TransportManager constructTransportManager() {
        StaticValidator.requireNonNull(scheduler, "Scheduled executor is not set");
        ScheduledExecutor scheduledExecutor = scheduler.get();
        StaticValidator.requireNonNull(scheduledExecutor, "Scheduled executor is not set");
        DelayService delayService = constructDelayService(scheduledExecutor);
        TransportRegistry transports = constructTransportRegistry();
        ServerRegistry servers = constructServerRegistry(transports);
        TransportRequestExecutor requestExecutor = constructTransportRequestExecutor(transports, scheduledExecutor);
        return new DefaultTransportManager(servers, requestExecutor, delayService, inputStreamMarkLimit);
    }

    private TransportRequestExecutor constructTransportRequestExecutor(
            TransportRegistry transports,
            ScheduledExecutor scheduledExecutor) {

        TimeoutService timeoutService = constructTimeoutService(scheduledExecutor);
        return new TransportRequestExecutor(transports, timeoutService);
    }

    private TransportRegistry constructTransportRegistry() {
        List<Transport> transports = this.transports.stream()
                .map(Supplier::get)
                .filter(transport -> {
                    if (transport == null) {
                        LOGGER.warn("Empty transport supplier found");
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        if (transports.isEmpty()) {
            throw new IllegalStateException("No transports specified");
        }
        return new TransportRegistry(transports);
    }

    private ServerRegistry constructServerRegistry(TransportRegistry transports) {
        StaticValidator.requireNonNull(serverProvider, "Server address provider is not set");
        StaticValidator.requireNonNull(loadBalancingPolicy, "Load balancing policy is not set");
        return new DefaultServerRegistry(serverProvider, transports, loadBalancingPolicy);
    }

    private TimeoutService constructTimeoutService(ScheduledExecutor executor) {
        return new DefaultTimeoutService(executor);
    }

    private DelayService constructDelayService(ScheduledExecutor executor) {
        return new DefaultDelayService(executor);
    }
}
