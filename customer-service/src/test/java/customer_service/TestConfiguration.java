package customer_service;

import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
