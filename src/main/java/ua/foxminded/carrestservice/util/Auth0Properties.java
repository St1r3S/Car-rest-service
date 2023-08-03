package ua.foxminded.carrestservice.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth0")
@Getter
@Setter
public class Auth0Properties {
    private String domain;
    private String clientId;
    private String clientSecret;
    private String audience;
}
