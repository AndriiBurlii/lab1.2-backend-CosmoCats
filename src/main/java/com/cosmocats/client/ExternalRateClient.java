
package com.cosmocats.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ExternalRateClient {
    private final RestClient rest;
    public ExternalRateClient() {
        this.rest = RestClient.create();
    }

    public String getStatus(String baseUrl) {
        ResponseEntity<String> resp = rest.get().uri(baseUrl + "/status").retrieve().toEntity(String.class);
        return resp.getBody();
    }
}
