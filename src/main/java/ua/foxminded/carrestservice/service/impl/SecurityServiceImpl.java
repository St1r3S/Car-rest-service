package ua.foxminded.carrestservice.service.impl;

import org.springframework.stereotype.Service;
import ua.foxminded.carrestservice.model.AuthTokenResponse;
import ua.foxminded.carrestservice.service.SecurityService;
import ua.foxminded.carrestservice.util.AuthTokenProvider;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final AuthTokenProvider authTokenProvider;

    public SecurityServiceImpl(AuthTokenProvider authTokenProvider) {
        this.authTokenProvider = authTokenProvider;
    }

    @Override
    public AuthTokenResponse getAuthToken() {
        return AuthTokenResponse.builder().token(authTokenProvider.getAuthToken().getToken()).build();
    }
}
