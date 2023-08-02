package ua.foxminded.carrestservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.carrestservice.config.SecurityConfig;
import ua.foxminded.carrestservice.model.Category;
import ua.foxminded.carrestservice.service.CategoryService;
import ua.foxminded.carrestservice.util.AuthTokenProvider;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CategoryRestControllerV1.class},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {AuthTokenProvider.class})
        })
@Import(SecurityConfig.class)
class CategoryRestControllerV1Test {

    @Autowired
    private AuthTokenProvider authTokenProvider;
    private String accessToken;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @PostConstruct
    void init() {
        accessToken = authTokenProvider.getAuthToken().getToken();
    }

    @Test
    void shouldCreateCategory() throws Exception {
        Category category = new Category("Sedan");

        when(categoryService.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(category))
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category").value("Sedan"));

        verify(categoryService, times(1)).save(any(Category.class));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Long categoryId = 1L;
        Category updatedCategory = new Category("Sports Car");

        when(categoryService.existsById(categoryId)).thenReturn(true);
        when(categoryService.save(any(Category.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCategory))
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Sports Car"));
    }

    @Test
    void shouldGetAllCategories() throws Exception {
        List<Category> categories = Arrays.asList(
                new Category("SUV"),
                new Category("Convertible")
        );

        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].category").value("SUV"))
                .andExpect(jsonPath("$[1].category").value("Convertible"));
    }

    @Test
    void shouldGetAllCategoriesWithPagination() throws Exception {
        List<Category> categories = Arrays.asList(
                new Category("SUV"),
                new Category("Convertible")
        );

        Pageable pageable = PageRequest.of(0, 2);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryService.findAll(any(Pageable.class))).thenReturn(categoryPage);

        mockMvc.perform(get("/api/v1/categories/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].category").value("SUV"))
                .andExpect(jsonPath("$.content[1].category").value("Convertible"));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        Long categoryId = 1L;

        when(categoryService.existsById(categoryId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
