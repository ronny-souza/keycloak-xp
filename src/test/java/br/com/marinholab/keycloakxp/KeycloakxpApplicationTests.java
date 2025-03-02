package br.com.marinholab.keycloakxp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles("test")
class KeycloakxpApplicationTests {

    @Test
    @DisplayName("Should load application contexts")
    void contextLoads() {
        // This method only tests application context loads on initialization
    }

    @Test
    @DisplayName("Should execute application main method")
    void shouldExecuteApplicationMainMethod() {
        System.setProperty("spring.profiles.active", "test");
        assertDoesNotThrow(() -> KeycloakxpApplication.main(new String[]{}));
    }

}
