package ua.foxminded.carrestservice.util;

import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ua.foxminded.carrestservice.model.AuthTokenResponse;
import ua.foxminded.carrestservice.model.auth0.Auth0Request;
import ua.foxminded.carrestservice.model.auth0.Auth0Response;

import java.util.Optional;

@Component
public class AuthTokenProvider {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Auth0Properties auth0Properties;

    private String tokenRequestUrl;

    public AuthTokenProvider(Auth0Properties auth0Properties) {
        this.auth0Properties = auth0Properties;
    }

    @PostConstruct
    void init() {
        tokenRequestUrl = "https://%s/oauth/token".formatted(auth0Properties.getDomain());
    }

    public AuthTokenResponse getAuthToken() {
        Auth0Request request = Auth0Request.builder()
                .clientId(auth0Properties.getClientId())
                .clientSecret(auth0Properties.getClientSecret())
                .audience(auth0Properties.getAudience())
                .build();

        ResponseEntity<Auth0Response> response = restTemplate
                .postForEntity(tokenRequestUrl, request, Auth0Response.class);

        String accessToken = Optional.ofNullable(response.getBody())
                .map(Auth0Response::getAccessToken)
                .orElseThrow(() -> new IllegalArgumentException("Unable to obtain Security token"));

        return AuthTokenResponse.builder().token(accessToken).build();
    }
}

