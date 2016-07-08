package me.etki.grac.implementation.transport.asynchttpclient;

import com.google.common.net.MediaType;
import me.etki.grac.common.Action;
import me.etki.grac.transport.Payload;
import me.etki.grac.transport.ResponseStatus;
import me.etki.grac.transport.ServerRequest;
import me.etki.grac.transport.ServerResponse;
import me.etki.grac.transport.Transport;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Param;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.uri.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * todo quick implementation
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class AsyncHttpClientTransport implements Transport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncHttpClientTransport.class);

    private final AsyncHttpClient client;

    public AsyncHttpClientTransport(AsyncHttpClient client) {
        this.client = client;
    }

    public AsyncHttpClientTransport() {
        this(new DefaultAsyncHttpClient());
    }

    @Override
    public CompletableFuture<ServerResponse> execute(ServerRequest serverRequest) {
        Request request = convertRequest(serverRequest);
        return client
                .executeRequest(request)
                .toCompletableFuture()
                .thenApply(response -> postprocess(request, response))
                .thenApply(this::convertResponse);
    }

    protected Response postprocess(Request request, Response response) {
        if (response.getStatusCode() == 404 && request.getMethod().equals("GET")) {
            return new ResponseWrapper(response) {
                @Override
                public int getStatusCode() {
                    return 200;
                }
            };
        }
        return response;
    }

    @Override
    public boolean supports(String protocol) {
        return "http".equals(protocol) || "https".equals(protocol);
    }

    private Request convertRequest(ServerRequest request) {
        String resource = request.getResource();
        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }
        String host = request.getServer().getAddress().getHostString();
        int port = request.getServer().getAddress().getPort();
        Uri uri = new Uri(request.getServer().getProtocol(), null, host, port, resource, null);
        RequestBuilder requestBuilder = new RequestBuilder(convertAction(request.getAction()))
                .setQueryParams(convertParameters(request.getParameters()))
                .setHeaders(convertMetadata(request.getMetadata()))
                .setUri(uri)
                .setRequestTimeout(request.getTimeout().intValue());
        if (!request.getAcceptedMimeTypes().isEmpty()) {
            requestBuilder.addHeader("Accept", assembleAcceptsHeader(request.getAcceptedMimeTypes()));
        }
        if (!request.getAcceptedLocales().isEmpty()) {
            requestBuilder.addHeader("Accept-Language", String.join(", ", request.getAcceptedLocales()));
        }
        if (request.getClientIdentifier().isPresent()) {
            //noinspection OptionalGetWithoutIsPresent
            requestBuilder.setHeader("User-Agent", request.getClientIdentifier().get());
        }
        return requestBuilder.build();
    }

    private Map<String, Collection<String>> convertMetadata(Map<String, List<Object>> metadata) {
        Function<Map.Entry<String, List<Object>>, Collection<String>> transformer
                = e -> e.getValue().stream().map(Object::toString).collect(Collectors.toList());
        return metadata.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, transformer));
    }

    private List<Param> convertParameters(Map<String, List<Object>> parameters) {
        return parameters.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(value -> new Param(entry.getKey(), value.toString())))
                .collect(Collectors.toList());
    }

    private String assembleAcceptsHeader(List<MediaType> acceptedMimeTypes) {
        return acceptedMimeTypes.stream()
                .map(MediaType::withoutParameters)
                .map(MediaType::toString)
                .collect(Collectors.joining(", "));
    }

    private ServerResponse convertResponse(Response response) {
        ServerResponse serverResponse = new ServerResponse()
                .setDescription(response.getStatusText());
        if (response.getStatusCode() > 499) {
            serverResponse.setStatus(ResponseStatus.SERVER_ERROR);
        } else if (response.getStatusCode() > 399) {
            serverResponse.setStatus(ResponseStatus.CLIENT_ERROR);
        } else {
            serverResponse.setStatus(ResponseStatus.OK);
        }
        if (response.hasResponseBody()) {
            MediaType mimeType = Optional.ofNullable(response.getContentType())
                    .map(MediaType::parse)
                    .orElse(MediaType.OCTET_STREAM);
            Long size = Optional.ofNullable(response.getHeader("Content-Length")).map(Long::valueOf).orElse(null);
            Payload payload = new Payload()
                    .setMimeType(mimeType)
                    .setSize(size)
                    .setContent(response.getResponseBodyAsStream());
            serverResponse.setPayload(payload);
        }
        Map<String, List<Object>> metadata = new HashMap<>();
        for (Map.Entry<String, String> entry : response.getHeaders()) {
            metadata.putIfAbsent(entry.getKey(), new ArrayList<>());
            metadata.get(entry.getKey()).add(entry.getValue());
        }
        return serverResponse.setMetadata(metadata);
    }

    private String convertAction(Action action) {
        switch (action) {
            case INSPECT:
                return "HEAD";
            case READ:
                return "GET";
            case CREATE:
                return "POST";
            case SET:
                return "PUT";
            case MODIFY:
                return "PATCH";
            case DELETE:
                return "DELETE";
            default:
                throw new IllegalArgumentException("Unknown action " + action);
        }
    }
}
