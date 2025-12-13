package com.cosmocats.security;

import com.cosmocats.CosmoCatsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CosmoCatsApplication.class)
@AutoConfigureMockMvc
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    // 1. Тест: Без токена має бути 401
    @Test
    void shouldReturn401_WhenNoAuthProvided() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }

    // 2. Тест: З валідним JWT токеном має бути 200
    @Test
    void shouldReturn200_WhenValidJwtProvided() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    // 3. Тест: З валідним API Key має бути 200 (Твій Варіант 1)
    @Test
    void shouldReturn200_WhenValidApiKeyProvided() throws Exception {
        String validKey = "cosmo-secret-key-123";

        mockMvc.perform(get("/api/v1/products")
                        .header("X-API-KEY", validKey))
                .andExpect(status().isOk());
    }

    // 4. Тест: З неправильним API Key має бути 401
    @Test
    void shouldReturn401_WhenInvalidApiKeyProvided() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .header("X-API-KEY", "wrong-key-ha-ha"))
                .andExpect(status().isUnauthorized());
    }
}