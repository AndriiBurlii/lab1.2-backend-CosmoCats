package com.cosmocats.wiremock;

import com.cosmocats.client.ExternalRateClient;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WireMockTest(httpPort = 8089)
class ExternalRateClientWireMockTest {

    @Autowired
    ExternalRateClient client;

    @Test
    void wiremock_stubbing_ok() {
        stubFor(get(urlEqualTo("/status"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody("OK")));

        String status = client.getStatus("http://localhost:8089");
        assertEquals("OK", status);
        verify(getRequestedFor(urlEqualTo("/status")));
    }
}
