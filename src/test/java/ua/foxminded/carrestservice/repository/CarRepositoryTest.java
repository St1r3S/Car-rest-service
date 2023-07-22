package ua.foxminded.carrestservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.carrestservice.model.Car;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/data_clean.sql", "/sql/data_sample.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CarRepositoryTest {

    @Autowired
    CarRepository carRepository;

    @Test
    void shouldVerifyRepositoryInjected() {
        assertNotNull(carRepository);
    }

    @Test
    void shouldFindCarsByFilters() {
        String manufacturer = "Chevrolet";
        String model = "Malibu";
        Integer minYear = 2019;
        Integer maxYear = 2021;
        String category = "Sedan";

        List<Car> cars = carRepository.findCarsByFilters(manufacturer, model, minYear, maxYear, category);

        assertEquals(1, cars.size());
        Car car = cars.get(0);
        assertEquals(manufacturer, car.getManufacturer().getMake());
        assertEquals(model, car.getModel());
        assertEquals(2020, car.getManufactureYear());
    }

    @Test
    void shouldFindByManufacturer() {
        String manufacturer = "Chevrolet";

        List<Car> cars = carRepository.findCarsByFilters(manufacturer, null, null, null, null);

        for (Car car : cars) {
            assertEquals(manufacturer, car.getManufacturer().getMake());
        }
    }

    @Test
    void shouldFindByModel() {
        String model = "Q3";

        List<Car> cars = carRepository.findCarsByFilters(null, model, null, null, null);

        assertEquals(1, cars.size());
        Car car = cars.get(0);
        assertEquals(model, car.getModel());
    }

    @Test
    void shouldFindByMinAndMaxYear() {
        Integer minYear = 2019;
        Integer maxYear = 2021;

        List<Car> cars = carRepository.findCarsByFilters(null, null, minYear, maxYear, null);

        assertEquals(10, cars.size());

        for (Car car : cars) {
            assertTrue(car.getManufactureYear() >= minYear && car.getManufactureYear() <= maxYear);
        }
    }

    @Test
    void shouldFindByCategory() {
        String category = "Sedan";

        List<Car> cars = carRepository.findCarsByFilters(null, null, null, null, category);

        assertEquals(3, cars.size());

        for (Car car : cars) {
            assertTrue(car.getCategories().stream().anyMatch(cat -> cat.getCategory().equals(category)));
        }
    }

    @Test
    void shouldFindByCombinedFilters() {
        String manufacturer = "Chevrolet";
        String model = "Malibu";
        Integer minYear = 2019;
        Integer maxYear = 2021;
        String category = "Sedan";

        List<Car> cars = carRepository.findCarsByFilters(manufacturer, model, minYear, maxYear, category);

        assertEquals(1, cars.size());
        Car car = cars.get(0);
        assertEquals(manufacturer, car.getManufacturer().getMake());
        assertEquals(model, car.getModel());
        assertTrue(car.getManufactureYear() >= minYear && car.getManufactureYear() <= maxYear);
        assertTrue(car.getCategories().stream().anyMatch(cat -> cat.getCategory().equals(category)));
    }
}
