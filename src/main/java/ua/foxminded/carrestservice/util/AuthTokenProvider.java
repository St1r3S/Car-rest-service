package ua.foxminded.carrestservice.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${auth0.domain}")
    private String domain;
    @Value("${auth0.clientId}")
    private String clientId;
    @Value("${auth0.clientSecret}")
    private String clientSecret;
    @Value("${auth0.audience}")
    private String audience;
    private String tokenRequestUrl;

    @PostConstruct
    void init() {
        tokenRequestUrl = "https://%s/oauth/token".formatted(domain);
    }

    public AuthTokenResponse getAuthToken() {
        Auth0Request request = Auth0Request.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .audience(audience)
                .build();

        ResponseEntity<Auth0Response> response = restTemplate
                .postForEntity(tokenRequestUrl, request, Auth0Response.class);

        String accessToken = Optional.ofNullable(response.getBody())
                .map(Auth0Response::getAccessToken)
                .orElseThrow(() -> new IllegalArgumentException("Unable to obtain Security token"));

        return AuthTokenResponse.builder().token(accessToken).build();
    }
}

