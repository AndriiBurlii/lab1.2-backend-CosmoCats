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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {CosmoCatsApplication.class, PostgresTestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String createProduct(String name, BigDecimal price) throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setPrice(price);
        request.setCategory("GADGETS");

        return mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.containsString("/api/v1/products/")))
                .andReturn()
                .getResponse()
                .getHeader("Location");
    }

    @Test
    void create_ok() throws Exception {
        createProduct("Phone-Create", BigDecimal.valueOf(100));
    }

    @Test
    void get_ok() throws Exception {
        String name = "Phone-Get";
        BigDecimal price = BigDecimal.valueOf(200);

        String location = createProduct(name, price);

        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.price").value(price.intValue()));
    }

    @Test
    void update_ok() throws Exception {
        String originalName = "Phone-Update";
        BigDecimal originalPrice = BigDecimal.valueOf(300);

        String location = createProduct(originalName, originalPrice);

        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Phone-Update-Updated");
        updateRequest.setPrice(BigDecimal.valueOf(350));
        updateRequest.setCategory("GADGETS");

        mvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone-Update-Updated"))
                .andExpect(jsonPath("$.price").value(350));
    }

    @Test
    void delete_ok() throws Exception {
        String location = createProduct("Phone-Delete", BigDecimal.valueOf(400));

        mvc.perform(delete(location))
                .andExpect(status().isNoContent());
    }

    @Test
    void create_invalid_returnsBadRequest() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("");
        request.setPrice(BigDecimal.valueOf(-1));
        request.setCategory("GADGETS");

        mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_nonExistingProduct_returnsNotFound() throws Exception {
        mvc.perform(get("/api/v1/products/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_nonExistingProduct_returnsNotFound() throws Exception {
        mvc.perform(delete("/api/v1/products/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_nonExistingProduct_returnsNotFound() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Does-not-matter");
        request.setPrice(BigDecimal.TEN);
        request.setCategory("GADGETS");

        mvc.perform(put("/api/v1/products/{id}", 999_999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_invalid_returnsBadRequest() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName(""); // невалідне ім'я
        request.setPrice(BigDecimal.valueOf(-5)); // невалідна ціна
        request.setCategory("GADGETS");

        mvc.perform(put("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
