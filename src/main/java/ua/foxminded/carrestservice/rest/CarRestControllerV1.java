package ua.foxminded.carrestservice.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.carrestservice.model.Car;
import ua.foxminded.carrestservice.model.Manufacturer;
import ua.foxminded.carrestservice.service.CarService;
import ua.foxminded.carrestservice.service.ManufacturerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarRestControllerV1 {


    private final CarService carService;
    private final ManufacturerService manufacturerService;


    public CarRestControllerV1(CarService carService, ManufacturerService manufacturerService) {
        this.carService = carService;
        this.manufacturerService = manufacturerService;
    }

    @PostMapping("/manufacturers/{manufacturer}/models/{model}/{year}")
    public ResponseEntity<Car> createCar(
            @PathVariable("manufacturer") String manufacturer,
            @PathVariable("model") String model,
            @PathVariable("year") Integer year
    ) {
        Manufacturer foundManufacturer = manufacturerService.findByMake(manufacturer);
        if (foundManufacturer == null) {
            return ResponseEntity.notFound().build();
        }

        Car car = new Car(model, year, foundManufacturer);
        Car savedCar = carService.save(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(
            @PathVariable("id") Long id,
            @RequestBody Car car
    ) {
        if (!carService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        car.setId(id);

        Car updatedCar = carService.save(car);
        return ResponseEntity.ok(updatedCar);
    }

    @GetMapping
    public ResponseEntity<List<Car>> searchCars(
            @RequestParam(value = "manufacturer", required = false) String manufacturer,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "minYear", required = false) Integer minYear,
            @RequestParam(value = "maxYear", required = false) Integer maxYear,
            @RequestParam(value = "category", required = false) String category
    ) {
        List<Car> cars = carService.findCarsByFilters(manufacturer, model, minYear, maxYear, category);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Car>> getAllCars(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String[] sort
    ) {
        String sortField = sort[0];
        String sortDirection = sort[1].equalsIgnoreCase("desc") ? "desc" : "asc";

        Sort sortObj = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Car> carPage = carService.findAll(pageable);
        return ResponseEntity.ok(carPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") Long id) {
        if (!carService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        carService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
