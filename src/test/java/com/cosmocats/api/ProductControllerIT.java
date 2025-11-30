package com.cosmocats.api;

import com.cosmocats.CosmoCatsApplication;
import com.cosmocats.api.dto.ProductRequest;
import com.cosmocats.config.PostgresTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = {CosmoCatsApplication.class, PostgresTestConfig.class})
@AutoConfigureMockMvc
class ProductControllerIT {


    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void fullCrudFlow_ok() throws Exception {
        // create
        ProductRequest req = new ProductRequest();
        req.setName("Phone");
        req.setPrice(new BigDecimal("123.45"));
        req.setCategory("tech");

        String body = mapper.writeValueAsString(req);

        String location = mvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/v1/products/")))
            .andReturn().getResponse().getHeader("Location");

        // list
        mvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Phone"));

        // update
        req.setName("Phone X");
        mvc.perform(put(location.replaceFirst(".*/api/v1/products/", "/api/v1/products/"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Phone X"));

        // delete
        mvc.perform(delete(location.replaceFirst(".*/api/v1/products/", "/api/v1/products/")))
            .andExpect(status().isNoContent());
    }

    @Test
    void create_invalid_returns400() throws Exception {
        ProductRequest bad = new ProductRequest(); // empty -> violates @Valid
        mvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bad)))
            .andExpect(status().isBadRequest());
    }
}
