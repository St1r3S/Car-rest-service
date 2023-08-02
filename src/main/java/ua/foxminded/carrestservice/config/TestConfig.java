package ua.foxminded.carrestservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.foxminded.carrestservice.util.AccessTokenProvider;

@Configuration
public class TestConfig {

    @Bean
    public AccessTokenProvider accessTokenProvider() {
        return AccessTokenProvider.getInstance();
    }
}
