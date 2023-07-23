package ua.foxminded.carrestservice.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.carrestservice.model.Car;
import ua.foxminded.carrestservice.model.Manufacturer;
import ua.foxminded.carrestservice.repository.CarRepository;
import ua.foxminded.carrestservice.service.CarService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CarServiceImpl.class})
class CarServiceImplTest {

    @Autowired
    CarService carService;

    @MockBean
    CarRepository carRepository;

    @Test
    void shouldFindCarsByFilters() {
        List<Car> cars = Arrays.asList(
                new Car("Camry", 2022, new Manufacturer("Toyota")),
                new Car("Corolla", 2023, new Manufacturer("Toyota"))
        );

        when(carRepository.findCarsByFilters("Toyota", "Camry", 2022, 2023, "category")).thenReturn(cars);

        List<Car> filteredCars = carService.findCarsByFilters("Toyota", "Camry", 2022, 2023, "category");

        verify(carRepository, times(1)).findCarsByFilters("Toyota", ("Camry"), 2022, 2023, "category");

        assertEquals(cars, filteredCars);
    }

    @Test
    void shouldFindCarsByFiltersWithAllFilters() {
        List<Car> cars = Arrays.asList(
                new Car("Camry", 2022, new Manufacturer("Toyota")),
                new Car("Corolla", 2023, new Manufacturer("Toyota"))
        );

        when(carRepository.findCarsByFilters("Toyota", "Camry", 2022, 2023, "category")).thenReturn(cars);

        List<Car> filteredCars = carService.findCarsByFilters("Toyota", "Camry", 2022, 2023, "category");

        verify(carRepository, times(1)).findCarsByFilters("Toyota", "Camry", 2022, 2023, "category");

        assertEquals(cars, filteredCars);
    }

    @Test
    void shouldFindCarsByFiltersWithManufacturerAndModel() {
        List<Car> cars = Arrays.asList(
                new Car("Camry", 2022, new Manufacturer("Toyota")),
                new Car("Camry", 2023, new Manufacturer("Lexus"))
        );

        when(carRepository.findCarsByFilters(eq("Toyota"), eq("Camry"), isNull(), isNull(), isNull())).thenReturn(cars);

        List<Car> filteredCars = carService.findCarsByFilters("Toyota", "Camry", null, null, null);

        verify(carRepository, times(1)).findCarsByFilters(eq("Toyota"), eq("Camry"), isNull(), isNull(), isNull());

        assertEquals(cars, filteredCars);
    }

    @Test
    void shouldFindCarsByFiltersWithMinYearAndCategory() {
        List<Car> cars = Arrays.asList(
                new Car("Camry", 2022, new Manufacturer("Toyota")),
                new Car("Corolla", 2023, new Manufacturer("Toyota"))
        );

        when(carRepository.findCarsByFilters(isNull(), isNull(), eq(2022), isNull(), eq("sedan"))).thenReturn(cars);

        List<Car> filteredCars = carService.findCarsByFilters(null, null, 2022, null, "sedan");

        verify(carRepository, times(1)).findCarsByFilters(isNull(), isNull(), eq(2022), isNull(), eq("sedan"));

        assertEquals(cars, filteredCars);
    }

    @Test
    void shouldFindCarsByFiltersWithNoFilters() {
        List<Car> cars = Arrays.asList(
                new Car("Camry", 2022, new Manufacturer("Toyota")),
                new Car("Corolla", 2023, new Manufacturer("Toyota"))
        );

        when(carRepository.findCarsByFilters(isNull(), isNull(), isNull(), isNull(), isNull())).thenReturn(cars);

        List<Car> filteredCars = carService.findCarsByFilters(null, null, null, null, null);

        verify(carRepository, times(1)).findCarsByFilters(isNull(), isNull(), isNull(), isNull(), isNull());

        assertEquals(cars, filteredCars);
    }
}
