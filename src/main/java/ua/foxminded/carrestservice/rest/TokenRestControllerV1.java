package ua.foxminded.carrestservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.carrestservice.model.AuthTokenResponse;
import ua.foxminded.carrestservice.service.SecurityService;

@RestController
@RequestMapping(path = "/api/v1/authenticate")
@RequiredArgsConstructor
public class TokenRestControllerV1 {

    private final SecurityService service;

    @GetMapping
    AuthTokenResponse response() {
        return service.getAuthToken();
    }

}
