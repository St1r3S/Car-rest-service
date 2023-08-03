package ua.foxminded.carrestservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.carrestservice.model.AuthTokenResponse;
import ua.foxminded.carrestservice.service.SecurityService;

@RestController
@RequestMapping(path = "/api/v1/authenticate")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "The Authentication API")
public class TokenRestControllerV1 {

    private final SecurityService service;

    @Operation(summary = "Get authentication token", tags = "Authentication")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication token retrieved successfully",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenResponse.class))
                    }
            ),
    })
    @GetMapping
    AuthTokenResponse getAuthToken() {
        return service.getAuthToken();
    }
}

