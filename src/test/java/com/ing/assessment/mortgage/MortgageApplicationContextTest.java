package com.ing.assessment.mortgage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("MortgageApplication context loads successfully")
class MortgageApplicationContextTest {

    @Test
    @DisplayName("Spring Boot context loads successfully")
    void contextLoads() {
        assertTrue(true);
    }
}
