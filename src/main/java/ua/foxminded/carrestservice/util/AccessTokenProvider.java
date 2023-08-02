package ua.foxminded.carrestservice.util;

import ua.foxminded.carrestservice.model.AuthTokenResponse;
import ua.foxminded.carrestservice.service.SecurityService;
import ua.foxminded.carrestservice.service.impl.SecurityServiceImpl;

public class AccessTokenProvider {

    private static AccessTokenProvider instance;
    private final String accessToken;

    private AccessTokenProvider() {
        accessToken = fetchAccessToken();
    }

    public static synchronized AccessTokenProvider getInstance() {
        if (instance == null) {
            instance = new AccessTokenProvider();
        }
        return instance;
    }

    public String getAccessToken() {
        return accessToken;
    }

    private String fetchAccessToken() {
        SecurityService securityService = new SecurityServiceImpl();
        AuthTokenResponse authTokenResponse = securityService.getAuthToken();
        return authTokenResponse.getToken();
    }
}

