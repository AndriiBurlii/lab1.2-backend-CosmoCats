package com.cosmocats.api;

import com.cosmocats.service.CategoryService;
import com.cosmocats.service.dto.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void list_ok() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setCode("space-food");
        dto.setTitle("Space Food");

        given(categoryService.list()).willReturn(List.of(dto));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].code").value("space-food"))
                .andExpect(jsonPath("$[0].title").value("Space Food"));
    }

    @Test
    void create_valid_returns201() throws Exception {
        CategoryDto request = new CategoryDto();
        request.setCode("weapons");
        request.setTitle("Laser Weapons");

        CategoryDto response = new CategoryDto();
        response.setId(42L);
        response.setCode("weapons");
        response.setTitle("Laser Weapons");

        given(categoryService.create(ArgumentMatchers.any(CategoryDto.class)))
                .willReturn(response);

        mockMvc.perform(
                        post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(42L))
                .andExpect(jsonPath("$.code").value("weapons"))
                .andExpect(jsonPath("$.title").value("Laser Weapons"));
    }
}
