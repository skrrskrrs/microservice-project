package customer_service.DTOs;

import java.util.UUID;

public record CreateCustomerDTO (String userName, String password, String firstName, String lastName, MailAddressDTO mailAddressDTO, HomeAddressDTO homeAddressDTO){

}
