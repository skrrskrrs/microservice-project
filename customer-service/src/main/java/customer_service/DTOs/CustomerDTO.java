package customer_service.DTOs;

import java.util.UUID;


public record CustomerDTO(UUID customerId, String firstName, String lastName, MailAddressDTO mailAddressDTO, HomeAddressDTO homeAddressDTO) {
}
