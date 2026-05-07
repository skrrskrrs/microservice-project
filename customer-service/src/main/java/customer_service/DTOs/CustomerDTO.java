package customer_service.DTOs;

import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;

import java.util.UUID;


public record CustomerDTO(UUID customerId, String firstName, String lastName, MailAddressDTO mailAddressDTO, HomeAddressDTO homeAddressDTO) {
    public static CustomerDTO createCustomerAsDTOWithId(UUID customerId, String firstName, String lastName, MailAddress mailAddress, HomeAddress homeAddress){
        return new CustomerDTO(customerId,firstName,lastName,MailAddressDTO.mailAddressAsDTO(mailAddress),HomeAddressDTO.homeAddressAsDTO(homeAddress));
    }
}
