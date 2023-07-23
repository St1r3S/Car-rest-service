package ua.foxminded.carrestservice.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.carrestservice.model.Manufacturer;
import ua.foxminded.carrestservice.service.ManufacturerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manufacturers")
public class ManufacturerRestControllerV1 {


    private final ManufacturerService manufacturerService;

    public ManufacturerRestControllerV1(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PostMapping
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) {
        Manufacturer savedManufacturer = manufacturerService.save(manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedManufacturer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> updateManufacturer(@PathVariable("id") Long id, @RequestBody Manufacturer manufacturer) {
        if (!manufacturerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        manufacturer.setId(id);
        Manufacturer updatedManufacturer = manufacturerService.save(manufacturer);
        return ResponseEntity.ok(updatedManufacturer);
    }

    @GetMapping
    public ResponseEntity<List<Manufacturer>> getAllManufacturers() {
        List<Manufacturer> manufacturers = manufacturerService.findAll();
        return ResponseEntity.ok(manufacturers);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Manufacturer>> getAllCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String[] sort
    ) {
        String sortField = sort[0];
        String sortDirection = sort[1].equalsIgnoreCase("desc") ? "desc" : "asc";

        Sort sortObj = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Manufacturer> manufacturerPage = manufacturerService.findAll(pageable);
        return ResponseEntity.ok(manufacturerPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable("id") Long id) {
        if (!manufacturerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        manufacturerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
