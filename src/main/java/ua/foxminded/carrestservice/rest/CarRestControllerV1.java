package ua.foxminded.carrestservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Tag(name = "Car", description = "The Car API")
public class CarRestControllerV1 {

    private final CarService carService;
    private final ManufacturerService manufacturerService;

    @Operation(summary = "Create a new car", tags = "Car")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Car created successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Manufacturer not found"),
    })
    @SecurityRequirement(name = "Bearer Authentication")
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

    @Operation(summary = "Update an existing car", tags = "Car")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Car updated successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Car not found"),
    })
    @SecurityRequirement(name = "Bearer Authentication")
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

    @Operation(summary = "Search cars based on various filters", tags = "Car")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cars found successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Car.class))
                            )
                    }
            ),
    })
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

    @Operation(summary = "Get all cars with pagination and sorting", tags = "Car")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cars retrieved successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Car.class))
                            )
                    }
            ),
    })
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

    @Operation(summary = "Delete a car by its ID", tags = "Car")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Car deleted successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Car not found"),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") Long id) {
        if (!carService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        carService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

