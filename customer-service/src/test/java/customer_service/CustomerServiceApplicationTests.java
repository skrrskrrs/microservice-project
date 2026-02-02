package customer_service;

import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerException;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;
import customer_service.idempotency.application.IdempotencyApplicationService;
import customer_service.idempotency.domain.Idempotency;
import customer_service.idempotency.domain.IdempotencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@DataJpaTest
class CustomerServiceApplicationTests {
    private Customer validCustomer;


    @Test
    void createInvalidCustomer() {
        assertThrows(CustomerException.class,
                () -> Customer.newInstance("Eeat"
                        ,"Test"
                        ,MailAddress.newInstance("asdsad.de")
                        ,HomeAddress.newInstance("Teststreet","Cologne","Westfalen","50937")));
        assertThrows(CustomerException.class,
                () -> Customer.newInstance("Eeat"
                        ,"Test"
                        ,MailAddress.newInstance("asdsa@web.de")
                        ,HomeAddress.newInstance(null,null,null,null)));
        assertThrows(CustomerException.class,
                () -> Customer.newInstance("Eeat"
                        ,"Test"
                        ,MailAddress.newInstance("asdsa@web.de")
                        ,HomeAddress.newInstance("","Cologne","Westfalen","50937")));
        assertThrows(CustomerException.class,
                () -> Customer.newInstance(""
                        ,"Test"
                        ,MailAddress.newInstance("asdsa@web.de")
                        ,HomeAddress.newInstance("Test","Cologne","Westfalen","50937")));
        assertThrows(CustomerException.class,
                () -> Customer.newInstance("Peter"
                        ,""
                        ,MailAddress.newInstance("asdsa@web.de")
                        ,HomeAddress.newInstance("Test","Cologne","Westfalen","50937")));
    }

    @Test
    void testDomainPrimitiveEquals(){
        MailAddress mailAddress = MailAddress.newInstance("test@test.de");
        MailAddress mailAddress2 = MailAddress.newInstance("test@test.de");
        assertEquals(mailAddress,mailAddress2);
        HomeAddress homeAddress = HomeAddress.newInstance("Teststreet","Cologne","Westfalen","50937");
        HomeAddress homeAddress2 = HomeAddress.newInstance("Teststreet","Cologne","Westfalen","50937");
        assertEquals(homeAddress,homeAddress2);
    }

    @Test
    void testDomainPrimitiveNotEquals(){
        MailAddress mailAddress = MailAddress.newInstance("test@test.de");
        MailAddress mailAddress2 = MailAddress.newInstance("test@tett.de");
        assertNotEquals(mailAddress,mailAddress2);
        HomeAddress homeAddress = HomeAddress.newInstance("Teststreet","Cologne","Westfalen","50937");
        HomeAddress homeAddress2 = HomeAddress.newInstance("Teststreet","Cologn","Westfalen","50937");
        assertNotEquals(homeAddress,homeAddress2);
    }


}
