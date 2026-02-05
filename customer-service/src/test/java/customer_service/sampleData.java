package customer_service;

import customer_service.customer.domain.Customer;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;

public class sampleData {

    public static Customer customerHans() {
        return Customer.of(
                "Hans",
                "Hans",
                MailAddress.of("mailAddress@web.de"),
                HomeAddress.of("testStreet", "testCity", "testState", "testZip")
        );
    }
}