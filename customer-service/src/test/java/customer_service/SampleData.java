package customer_service;

import customer_service.DTOs.CreateCustomerDTO;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.Customer;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;

public class SampleData {

    public static Customer customerHans() {
        return Customer.of(
                "Hans",
                "Hans",
                MailAddress.of("mailAddress@web.de"),
                HomeAddress.of("testStreet", "testCity", "testState", "testZip")
        );
    }

    public static CreateCustomerDTO customerDTOHans() {
        return new CreateCustomerDTO("Hans",
                "Meier",
                MailAddressDTO.mailAddressAsDTO(MailAddress.of("test@web.de")),
                HomeAddressDTO.homeAddressAsDTO(HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937"))
        );
    }


}