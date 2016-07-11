package me.etki.grac.client;

import me.etki.grac.Client;
import me.etki.grac.ClientBuilder;
import me.etki.grac.Response;
import me.etki.grac.concurrent.CompletableFutures;
import me.etki.grac.implementation.serializer.JavaNativeSerializer;
import me.etki.grac.infrastructure.Responses;
import me.etki.grac.infrastructure.ServerProviders;
import me.etki.grac.infrastructure.Transports;
import me.etki.grac.io.SerializationResult;
import me.etki.grac.io.Serializer;
import me.etki.grac.transport.Payload;
import me.etki.grac.transport.ResponseStatus;
import me.etki.grac.transport.ServerRequest;
import me.etki.grac.transport.ServerResponse;
import me.etki.grac.transport.Transport;
import me.etki.grac.utility.TypeSpec;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ClientFunctionalTest {

    @Test
    public void shouldCorrectlyProcessRequest() throws Exception {
        Map<String, Object> value = new HashMap<>();
        value.put("integer", 2);
        value.put("boolean", false);
        Serializer serializer = new JavaNativeSerializer();
        SerializationResult serializationResult = serializer.serialize(value, JavaNativeSerializer.MIME_TYPE);
        Payload responsePayload = new Payload()
                .setMimeType(serializationResult.getMimeType())
                .setContent(serializationResult.getContent())
                .setSize(serializationResult.getSize());
        Transport transport = Transports.repeater(Responses.server(ResponseStatus.OK, responsePayload));
        Client client = new ClientBuilder()
                .withTransport(transport)
                .withServerProvider(ServerProviders.localhost())
                .withSerializer(serializer)
                .withDefaultSerializationType(JavaNativeSerializer.MIME_TYPE)
                .build();
        TypeSpec spec = new TypeSpec(Map.class, new TypeSpec(String.class), new TypeSpec(Object.class));
        Response<Map<String, Object>> response = client
                .<Map<String, Object>, Map<String, Object>>create("/test-resource", value, spec).get();
        Optional<Map<String, Object>> result = response.getResult();
        assertTrue(result.isPresent());
        assertEquals(value, result.get());
    }

    @Test
    public void shouldCorrectlyPassClientIdentifier() throws Exception {
        String identifier = "GRAC";
        Transport transport = Transports.mockForAnyProtocol();
        ArgumentCaptor<ServerRequest> captor = ArgumentCaptor.forClass(ServerRequest.class);
        when(transport.execute(captor.capture()))
                .thenReturn(CompletableFutures.completed(new ServerResponse().setStatus(ResponseStatus.OK)));
        new ClientBuilder()
                .withTransport(transport)
                .withServerProvider(ServerProviders.localhost())
                .withClientIdentifier(identifier)
                .build()
                .read("/test", new TypeSpec(Void.class))
                .get();
        Optional<String> capturedIdentifier = captor.getValue().getClientIdentifier();
        assertTrue(capturedIdentifier.isPresent());
        assertEquals(identifier, capturedIdentifier.get());
    }
}
