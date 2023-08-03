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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.carrestservice.config.SecurityConfig;
import ua.foxminded.carrestservice.model.Manufacturer;
import ua.foxminded.carrestservice.service.ManufacturerService;
import ua.foxminded.carrestservice.util.AuthTokenProvider;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ManufacturerRestControllerV1.class},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {AuthTokenProvider.class})
        })
@Import(SecurityConfig.class)
class ManufacturerRestControllerV1Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthTokenProvider authTokenProvider;
    @MockBean
    private ManufacturerService manufacturerService;

    private String accessToken;

    @PostConstruct
    void init() {
        accessToken = authTokenProvider.getAuthToken().getToken();
    }

    @Test
    void shouldCreateManufacturer() throws Exception {
        Manufacturer manufacturer = new Manufacturer("Toyota");

        when(manufacturerService.save(any(Manufacturer.class))).thenReturn(manufacturer);

        mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(manufacturer))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.make").value("Toyota"));

        verify(manufacturerService, times(1)).save(any(Manufacturer.class));
    }

    @Test
    void shouldUpdateManufacturer() throws Exception {
        Long manufacturerId = 1L;
        Manufacturer updatedManufacturer = new Manufacturer("Honda");

        when(manufacturerService.existsById(manufacturerId)).thenReturn(true);
        when(manufacturerService.save(any(Manufacturer.class))).thenReturn(updatedManufacturer);

        mockMvc.perform(put("/api/v1/manufacturers/{id}", manufacturerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedManufacturer))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Honda"));
    }

    @Test
    void shouldGetAllManufacturers() throws Exception {
        List<Manufacturer> manufacturers = Arrays.asList(
                new Manufacturer("Toyota"),
                new Manufacturer("Honda")
        );

        when(manufacturerService.findAll()).thenReturn(manufacturers);

        mockMvc.perform(get("/api/v1/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].make").value("Toyota"))
                .andExpect(jsonPath("$[1].make").value("Honda"));
    }

    @Test
    void shouldGetAllManufacturersWithPagination() throws Exception {
        List<Manufacturer> manufacturers = Arrays.asList(
                new Manufacturer("Toyota"),
                new Manufacturer("Honda")
        );

        Pageable pageable = PageRequest.of(0, 2);
        Page<Manufacturer> manufacturerPage = new PageImpl<>(manufacturers, pageable, manufacturers.size());

        when(manufacturerService.findAll(any(Pageable.class))).thenReturn(manufacturerPage);

        mockMvc.perform(get("/api/v1/manufacturers/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].make").value("Toyota"))
                .andExpect(jsonPath("$.content[1].make").value("Honda"));
    }

    @Test
    void shouldDeleteManufacturer() throws Exception {
        Long manufacturerId = 1L;

        when(manufacturerService.existsById(manufacturerId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/manufacturers/{id}", manufacturerId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
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
