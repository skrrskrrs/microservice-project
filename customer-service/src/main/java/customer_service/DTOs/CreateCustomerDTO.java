package customer_service.DTOs;

public record CreateCustomerDTO (String firstName, String lastName, MailAddressDTO mailAddressDTO, HomeAddressDTO homeAddressDTO){

}
