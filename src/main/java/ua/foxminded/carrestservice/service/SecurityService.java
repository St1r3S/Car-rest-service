package ua.foxminded.carrestservice.service;


import ua.foxminded.carrestservice.model.AuthTokenResponse;

public interface SecurityService {
    AuthTokenResponse getAuthToken();
}
