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
import ua.foxminded.carrestservice.model.Manufacturer;
import ua.foxminded.carrestservice.service.ManufacturerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manufacturers")
@RequiredArgsConstructor
@Tag(name = "Manufacturer", description = "The Manufacturer API")
public class ManufacturerRestControllerV1 {

    private final ManufacturerService manufacturerService;

    @Operation(summary = "Create a new manufacturer", tags = "Manufacturer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Manufacturer created successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Manufacturer.class))
                    }
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) {
        Manufacturer savedManufacturer = manufacturerService.save(manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedManufacturer);
    }

    @Operation(summary = "Update an existing manufacturer", tags = "Manufacturer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturer updated successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Manufacturer.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Manufacturer not found"),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> updateManufacturer(@PathVariable("id") Long id, @RequestBody Manufacturer manufacturer) {
        if (!manufacturerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        manufacturer.setId(id);
        Manufacturer updatedManufacturer = manufacturerService.save(manufacturer);
        return ResponseEntity.ok(updatedManufacturer);
    }

    @Operation(summary = "Get all manufacturers", tags = "Manufacturer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturers retrieved successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Manufacturer.class))
                            )
                    }
            ),
    })
    @GetMapping
    public ResponseEntity<List<Manufacturer>> getAllManufacturers() {
        List<Manufacturer> manufacturers = manufacturerService.findAll();
        return ResponseEntity.ok(manufacturers);
    }

    @Operation(summary = "Get all manufacturers with pagination and sorting", tags = "Manufacturer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturers retrieved successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Manufacturer.class))
                            )
                    }
            ),
    })
    @GetMapping("/list")
    public ResponseEntity<Page<Manufacturer>> getAllManufacturers(
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

    @Operation(summary = "Delete a manufacturer by its ID", tags = "Manufacturer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Manufacturer deleted successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Manufacturer.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Manufacturer not found"),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable("id") Long id) {
        if (!manufacturerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        manufacturerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

