package me.etki.grac.implementation.transport.asynchttpclient;

import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.Response;
import org.asynchttpclient.cookie.Cookie;
import org.asynchttpclient.uri.Uri;

import java.io.InputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ResponseWrapper implements Response {

    private final Response response;

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public String getStatusText() {
        return response.getStatusText();
    }

    @Override
    public byte[] getResponseBodyAsBytes() {
        return response.getResponseBodyAsBytes();
    }

    @Override
    public ByteBuffer getResponseBodyAsByteBuffer() {
        return response.getResponseBodyAsByteBuffer();
    }

    @Override
    public InputStream getResponseBodyAsStream() {
        return response.getResponseBodyAsStream();
    }

    @Override
    public String getResponseBody(Charset charset) {
        return response.getResponseBody(charset);
    }

    @Override
    public String getResponseBody() {
        return response.getResponseBody();
    }

    @Override
    public Uri getUri() {
        return response.getUri();
    }

    @Override
    public String getContentType() {
        return response.getContentType();
    }

    @Override
    public String getHeader(String name) {
        return response.getHeader(name);
    }

    @Override
    public List<String> getHeaders(String name) {
        return response.getHeaders(name);
    }

    @Override
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }

    @Override
    public boolean isRedirected() {
        return response.isRedirected();
    }

    @Override
    public List<Cookie> getCookies() {
        return response.getCookies();
    }

    @Override
    public boolean hasResponseStatus() {
        return response.hasResponseStatus();
    }

    @Override
    public boolean hasResponseHeaders() {
        return response.hasResponseHeaders();
    }

    @Override
    public boolean hasResponseBody() {
        return response.hasResponseBody();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return response.getRemoteAddress();
    }

    @Override
    public SocketAddress getLocalAddress() {
        return response.getLocalAddress();
    }
}
