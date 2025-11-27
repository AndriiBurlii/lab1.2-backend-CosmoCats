
package com.cosmocats.wiremock;

import com.cosmocats.client.ExternalRateClient;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8089)
class ExternalRateClientWireMockTest {

    @Test
    void wiremock_stubbing_ok() {
        // Arrange
        configureFor("localhost", 8089);
        stubFor(get(urlEqualTo("/status"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody("OK")));
        ExternalRateClient client = new ExternalRateClient();

        // Act
        String status = client.getStatus("http://localhost:8089");

        // Assert
        assertEquals("OK", status);
        verify(getRequestedFor(urlEqualTo("/status")));
    }
}
