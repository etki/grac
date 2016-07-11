package me.etki.grac.client;

import me.etki.grac.ClientBuilder;
import me.etki.grac.infrastructure.ServerProviders;
import me.etki.grac.infrastructure.Transports;
import org.junit.Test;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ClientBuilderFunctionalTest {

    @Test(expected = IllegalStateException.class)
    public void shouldNotCreateClientWithoutAddressProvider() {
        new ClientBuilder()
                .withTransport(Transports.dummy())
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotCreateClientWithoutTransport() {
        new ClientBuilder()
                .withServerProvider(ServerProviders.empty())
                .build();
    }

    @Test
    public void shouldSuccessfullyCreateWithOnlyTransportAndAddressProvider() {
        new ClientBuilder()
                .withServerProvider(ServerProviders.empty())
                .withTransport(Transports.dummy())
                .build();
    }
}
