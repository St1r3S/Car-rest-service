package ua.foxminded.carrestservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.carrestservice.model.Car;
import ua.foxminded.carrestservice.model.Manufacturer;
import ua.foxminded.carrestservice.service.CarService;
import ua.foxminded.carrestservice.service.ManufacturerService;
import ua.foxminded.carrestservice.util.AccessTokenProvider;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CarRestControllerV1.class)
class CarRestControllerV1Test {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarService carService;
    @MockBean
    private ManufacturerService manufacturerService;

    private static String accessToken;

    @BeforeAll
    static void setUp() {
        AccessTokenProvider accessTokenProvider = AccessTokenProvider.getInstance();
        accessToken = accessTokenProvider.getAccessToken();
    }

    @Test
    void shouldCreateCar() throws Exception {
        Manufacturer manufacturer = new Manufacturer("Toyota");
        Car car = new Car("Camry", 2023, manufacturer);

        when(manufacturerService.findByMake(anyString())).thenReturn(manufacturer);
        when(carService.save(any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/api/v1/cars/manufacturers/{manufacturer}/models/{model}/{year}", "Toyota", "Camry", 2023)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model").value("Camry"))
                .andExpect(jsonPath("$.manufactureYear").value(2023))
                .andExpect(jsonPath("$.manufacturer.make").value("Toyota"));

        verify(manufacturerService, times(1)).findByMake(manufacturer.getMake());
        verify(carService, times(1)).save(any(Car.class));
    }

    @Test
    void shouldUpdateCar() throws Exception {
        Long carId = 1L;
        Car updatedCar = new Car("Corolla", 2023, new Manufacturer("Toyota"));

        when(carService.existsById(carId)).thenReturn(true);
        when(carService.save(any(Car.class))).thenReturn(updatedCar);

        mockMvc.perform(put("/api/v1/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCar))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.manufactureYear").value(2023))
                .andExpect(jsonPath("$.manufacturer.make").value("Toyota"));
    }

    @Test
    void shouldSearchCars() throws Exception {
        List<Car> cars = Arrays.asList(
                new Car("Camry", 2022, new Manufacturer("Toyota")),
                new Car("Corolla", 2023, new Manufacturer("Toyota"))
        );

        when(carService.findCarsByFilters(anyString(), nullable(String.class), anyInt(), anyInt(), nullable(String.class))).thenReturn(cars);

        mockMvc.perform(get("/api/v1/cars")
                        .param("manufacturer", "Toyota")
                        .param("minYear", "2022")
                        .param("maxYear", "2023")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].model").value("Camry"))
                .andExpect(jsonPath("$[0].manufactureYear").value(2022))
                .andExpect(jsonPath("$[1].model").value("Corolla"))
                .andExpect(jsonPath("$[1].manufactureYear").value(2023));
    }

    @Test
    void shouldGetAllCars() throws Exception {
        List<Car> cars = Arrays.asList(
                new Car("Camry", 2022, new Manufacturer("Toyota")),
                new Car("Corolla", 2023, new Manufacturer("Toyota"))
        );
        Pageable pageable = PageRequest.of(0, 2);

        Page<Car> carPage = new PageImpl<>(cars, pageable, cars.size());
        when(carService.findAll(any(Pageable.class))).thenReturn(carPage);

        mockMvc.perform(get("/api/v1/cars/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].model").value("Camry"))
                .andExpect(jsonPath("$.content[0].manufactureYear").value(2022))
                .andExpect(jsonPath("$.content[1].model").value("Corolla"))
                .andExpect(jsonPath("$.content[1].manufactureYear").value(2023));
    }

    @Test
    void shouldDeleteCar() throws Exception {
        Long carId = 1L;

        when(carService.existsById(carId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/cars/{id}", carId))
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

