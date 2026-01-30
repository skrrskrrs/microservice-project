package invoice_service.components;

import invoice_service.DTOs.CustomerDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomerClient {

    private final WebClient webClient;

    public CustomerClient(WebClient customerWebClient) {
        this.webClient = customerWebClient;
    }

    public boolean exists(UUID customerId) {
        return Boolean.TRUE.equals(webClient
                .get()
                .uri("/customers/{id}", customerId)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just(true);
                    }
                    if (response.statusCode().value() == 404) {
                        return Mono.just(false);
                    }
                    return response.createException().flatMap(Mono::error);
                })
                .block());
    }

    public CustomerDTO getCustomerById(UUID id) {
        return webClient.get()
                .uri("/customers/{id}", id)
                .retrieve()
                .bodyToMono(CustomerDTO.class)
                .block();
    }
}
