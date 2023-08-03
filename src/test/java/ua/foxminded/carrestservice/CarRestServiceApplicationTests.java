package ua.foxminded.carrestservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.carrestservice.service.SecurityService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CarRestServiceApplicationTests {

    @Autowired
    SecurityService service;

    @Test
    void contextLoads() {
        assertNotNull(service);
    }

}
