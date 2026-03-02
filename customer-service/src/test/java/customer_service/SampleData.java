package customer_service;

import customer_service.DTOs.CreateCustomerDTO;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.Customer;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;
import customer_service.customer.domainprimitives.UserId;

import java.util.UUID;

public class SampleData {

    public static CreateCustomerDTO customerDTOHans() {
        return new CreateCustomerDTO("HansX",
                "1234",
                "Hans",
                "Peter",
                MailAddressDTO.mailAddressAsDTO(MailAddress.of("test@web.de")),
                HomeAddressDTO.homeAddressAsDTO(HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937"))
        );
    }


}