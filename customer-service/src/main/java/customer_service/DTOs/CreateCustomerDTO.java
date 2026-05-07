package customer_service.DTOs;


public record CreateCustomerDTO (String userName, String password, String firstName, String lastName, MailAddressDTO mailAddressDTO, HomeAddressDTO homeAddressDTO){ }