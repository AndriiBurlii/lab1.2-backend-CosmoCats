package com.cosmocats.api;

import com.cosmocats.CosmoCatsApplication;
import com.cosmocats.api.dto.ProductRequest;
import com.cosmocats.config.PostgresTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath; // Треба для парсингу ID
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {CosmoCatsApplication.class, PostgresTestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
class ProductControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Integer createProductAndGetId(String name, BigDecimal price) throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setPrice(price);
        request.setCategory("FOOD");

        String responseJson = mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(responseJson, "$.id");
    }

    @Test
    void create_validProduct_returnsCreated() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Space Food");
        request.setPrice(BigDecimal.TEN);
        request.setCategory("FOOD");

        mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Space Food")));
    }

    @Test
    void get_existingProduct_returnsOk() throws Exception {
        Integer id = createProductAndGetId("Test Get", BigDecimal.ONE);

        mvc.perform(get("/api/v1/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Get"))
                .andExpect(jsonPath("$.price").value(BigDecimal.ONE.intValue()));
    }

    @Test
    void update_existingProduct_updatesAndReturnsOk() throws Exception {
        Integer id = createProductAndGetId("Old Name", BigDecimal.TEN);

        ProductRequest updateReq = new ProductRequest();
        updateReq.setName("New Name");
        updateReq.setPrice(BigDecimal.valueOf(20));
        updateReq.setCategory("TOYS");

        mvc.perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(20).intValue()));
    }

    @Test
    void delete_existingProduct_returnsNoContent() throws Exception {
        Integer id = createProductAndGetId("To Delete", BigDecimal.TEN);

        mvc.perform(delete("/api/v1/products/{id}", id))
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
    @WithMockUser(roles = "USER")
    void delete_asUser_returnsForbidden() throws Exception {
        mvc.perform(delete("/api/v1/products/{id}", 999))
                .andExpect(status().isForbidden());
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
