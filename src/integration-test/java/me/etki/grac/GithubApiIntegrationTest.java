package me.etki.grac;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import me.etki.grac.implementation.serializer.JacksonSerializer;
import me.etki.grac.implementation.transport.asynchttpclient.AsyncHttpClientTransport;
import me.etki.grac.infrastructure.ServerProviders;
import me.etki.grac.infrastructure.dto.github.RateLimit;
import me.etki.grac.utility.TypeSpec;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * todo: tests should not rely on external infrastructure
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class GithubApiIntegrationTest {

    @Test
    public void shouldPullGithubOrganizations() throws Exception {
        Client client = new ClientBuilder()
                .withTransport(new AsyncHttpClientTransport())
                .withServerProvider(ServerProviders.github())
                .withSerializer(new JacksonSerializer(new ObjectMapper()))
                .withAcceptedLocale("en")
                .withDefaultAcceptedType(MediaType.JSON_UTF_8)
                .withClientIdentifier("test")
                .withDefaultTimeout(60 * 1000)
                .build();
        Optional<RateLimit> result = client
                .<RateLimit>read("/rate_limit", new TypeSpec(RateLimit.class))
                .get()
                .getResult();
        assertTrue(result.isPresent());
    }
}
