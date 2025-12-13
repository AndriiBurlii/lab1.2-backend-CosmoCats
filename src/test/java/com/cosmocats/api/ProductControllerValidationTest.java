package com.cosmocats.api;

import com.cosmocats.api.dto.ProductRequest;
import com.cosmocats.api.dto.ProductResponse;
import com.cosmocats.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerValidationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ProductService productService;

    @Test
    void create_validRequest_returns201() throws Exception {
        ProductRequest req =
                new ProductRequest("Ship", new BigDecimal("10.00"), "GADGETS");

        Mockito.when(productService.create(any()))
                .thenReturn(new ProductResponse(1L, "Ship", new BigDecimal("10.00"), "GADGETS"));

        mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ship"));
    }

    @Test
    void create_invalidRequest_returns400() throws Exception {
        // name порожнє, price негативне, category порожня
        String body = """
                {
                  "name": "",
                  "price": -1,
                  "category": ""
                }
                """;

        mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_invalidRequest_returns400() throws Exception {
        String body = """
                {
                  "name": "",
                  "price": -1,
                  "category": ""
                }
                """;

        mvc.perform(put("/api/v1/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void list_ok() throws Exception {
        Mockito.when(productService.list()).thenReturn(List.of(
                new ProductResponse(1L, "A", new BigDecimal("1.00"), "c")
        ));

        mvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("A"));
    }
}
