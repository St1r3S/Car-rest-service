package ua.foxminded.carrestservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.foxminded.carrestservice.model.AuthTokenResponse;
import ua.foxminded.carrestservice.service.SecurityService;
import ua.foxminded.carrestservice.util.AuthTokenProvider;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthTokenProvider authTokenProvider;

    @Override
    public AuthTokenResponse getAuthToken() {
        return AuthTokenResponse.builder().token(authTokenProvider.getAuthToken().getToken()).build();
    }
}
