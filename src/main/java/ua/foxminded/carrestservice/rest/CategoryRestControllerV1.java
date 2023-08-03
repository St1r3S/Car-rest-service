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
import ua.foxminded.carrestservice.model.Category;
import ua.foxminded.carrestservice.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "The Category API")
public class CategoryRestControllerV1 {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category", tags = "Category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
                    }
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @Operation(summary = "Update an existing category", tags = "Category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Category not found"),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable("id") Long id,
            @RequestBody Category updatedCategory
    ) {
        if (!categoryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        updatedCategory.setId(id);

        Category savedCategory = categoryService.save(updatedCategory);
        return ResponseEntity.ok(savedCategory);
    }

    @Operation(summary = "Get all categories", tags = "Category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categories retrieved successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Category.class))
                            )
                    }
            ),
    })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get all categories with pagination and sorting", tags = "Category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categories retrieved successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Category.class))
                            )
                    }
            ),
    })
    @GetMapping("/list")
    public ResponseEntity<Page<Category>> getAllCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String[] sort
    ) {
        String sortField = sort[0];
        String sortDirection = sort[1].equalsIgnoreCase("desc") ? "desc" : "asc";

        Sort sortObj = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Category> categoriesPage = categoryService.findAll(pageable);
        return ResponseEntity.ok(categoriesPage);
    }

    @Operation(summary = "Delete a category by its ID", tags = "Category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Category deleted successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Category not found"),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        if (!categoryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


