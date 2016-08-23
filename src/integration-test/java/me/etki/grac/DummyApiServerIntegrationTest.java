package me.etki.grac;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import me.etki.grac.exception.InvalidResponseFormatException;
import me.etki.grac.implementation.serializer.JacksonSerializer;
import me.etki.grac.implementation.transport.asynchttpclient.AsyncHttpClientTransport;
import me.etki.grac.infrastructure.ServerProviders;
import me.etki.grac.infrastructure.dummy_api_server.Routing;
import me.etki.grac.infrastructure.dummy_api_server.dto.JsonProblem;
import me.etki.grac.infrastructure.dummy_api_server.dto.User;
import me.etki.grac.utility.TypeSpec;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DummyApiServerIntegrationTest {

    private final static TypeSpec USER_TYPE = new TypeSpec(User.class);
    private final static TypeSpec JSON_PROBLEM_TYPE = new TypeSpec(JsonProblem.class);
    private final static TypeSpec VOID_TYPE = new TypeSpec(Void.class);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldSuccessfullyFetchUsers() throws Exception {

        Client client = new ClientBuilder()
                .withTransport(new AsyncHttpClientTransport(new DefaultAsyncHttpClient()))
                .withSerializer(new JacksonSerializer(new ObjectMapper()))
                .withDefaultSerializationType(MediaType.JSON_UTF_8)
                .withServerProvider(ServerProviders.dummyApiServer())
                .build();
        TypeSpec typeSpec = new TypeSpec(List.class, new TypeSpec(User.class));
        Response<List<User>> response = client.<List<User>>read("/api/structure/user", typeSpec).get();
        Optional<List<User>> result = response.getResult();
        assertTrue(result.isPresent());
        assertFalse(result.get().isEmpty());
    }

    @SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
    @Test
    public void shouldSuccessfullyRecoverFromError() throws Exception {
        Client client = new ClientBuilder()
                .withTransport(new AsyncHttpClientTransport(new DefaultAsyncHttpClient()))
                .withSerializer(new JacksonSerializer(new ObjectMapper()))
                .withDefaultSerializationType(MediaType.JSON_UTF_8)
                .withServerProvider(ServerProviders.dummyApiServer())
                .withFallbackType(JSON_PROBLEM_TYPE)
                .shouldThrowOnInvalidResponsePayloadType(false)
                .build();
        Response<User> response = client.<User>read("/api/structure/user/missing", USER_TYPE).get();
        assertFalse(response.getResult().isPresent());
        assertTrue(response.getAltResult().isPresent());
        assertThat(response.getAltResult().get(), isA((Class) JsonProblem.class));
    }

    @Test
    public void shouldResultInExceptionInCaseOfInvalidPayload() throws Exception {

        expectedException.expectCause(isA(InvalidResponseFormatException.class));

        Client client = fallbackAwareBuilder()
                .shouldThrowOnInvalidResponsePayloadType(true)
                .shouldThrowOnServerError(false)
                .shouldThrowOnClientError(false)
                .build();
        client.<User>read(Routing.JSON_PROBLEM_RESOURCE + "500", USER_TYPE).get();
    }

    @Test
    public void shouldTimeoutWithCorrespondingException() throws Exception {
        expectedException.expectCause(isA(TimeoutException.class));
        builder().withDefaultTimeout(100).build().read(Routing.TIMEOUT_RESOURCE + "1000", VOID_TYPE).get();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldNotThrowExceptionOnServerErrorIfConfigured() throws Exception {

        Client client = fallbackAwareBuilder()
                .shouldThrowOnInvalidResponsePayloadType(false)
                .shouldThrowOnServerError(false)
                .shouldThrowOnClientError(false)
                .build();

        Response<User> response = client.<User>read(Routing.JSON_PROBLEM_RESOURCE + "500", USER_TYPE).get();
        assertFalse(response.getResult().isPresent());
        assertTrue(response.getAltResult().isPresent());
        assertThat(response.getAltResult().get(), instanceOf(JsonProblem.class));
    }

    @Test
    public void shouldCorrectlyWorkWithNoPayloadResponse() throws Exception {
        Response<User> response = builder().build().<User>read(Routing.NULL_RESOURCE, USER_TYPE).get();
        assertFalse(response.getResult().isPresent());
        assertFalse(response.getAltResult().isPresent());
    }

    @Test
    public void shouldCorrectlyPassRequestPayload() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("alpha", 1);
        payload.put("beta", Arrays.asList(2, 2));
        payload.put("gamma", new HashMap<String, String>() {{ put("orly?", "yarly"); }});
        Response<Object> response = builder()
                .build()
                .set(Routing.PAYLOAD_MIRROR_RESOURCE, payload, new TypeSpec(Object.class))
                .get();
        assertTrue(response.getResult().isPresent());
        assertEquals(payload, response.getResult().get());
    }

    private static ClientBuilder builder() {
        return new ClientBuilder()
                .withTransport(new AsyncHttpClientTransport(new DefaultAsyncHttpClient()))
                .withSerializer(new JacksonSerializer(new ObjectMapper()))
                .withDefaultSerializationType(MediaType.JSON_UTF_8)
                .withServerProvider(ServerProviders.dummyApiServer());
    }
    private static ClientBuilder fallbackAwareBuilder() {
        return builder()
                .withFallbackType(JSON_PROBLEM_TYPE);
    }
}
