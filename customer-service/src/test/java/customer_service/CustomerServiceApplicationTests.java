package customer_service;

import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerException;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.CustomerId;
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
    @Test
    void createInvalidCustomer() {
        //given
        //when
        //then
        assertThrows(CustomerException.class,
                () -> Customer.of("Eeat"
                        ,"Test"
                        ,MailAddress.of("asdsad.de")
                        ,HomeAddress.of("Teststreet","Cologne","Westfalen","50937")));

        assertThrows(CustomerException.class,
                () -> Customer.of("Eeat"
                        ,"Test"
                        ,MailAddress.of("asdsa@web.de")
                        ,HomeAddress.of(null,null,null,null)));

        assertThrows(CustomerException.class,
                () -> Customer.of("Eeat"
                        ,"Test"
                        ,MailAddress.of("asdsa@web.de")
                        ,HomeAddress.of("","Cologne","Westfalen","50937")));

        assertThrows(CustomerException.class,
                () -> Customer.of(""
                        ,"Test"
                        ,MailAddress.of("asdsa@web.de")
                        ,HomeAddress.of("Test","Cologne","Westfalen","50937")));

        assertThrows(CustomerException.class,
                () -> Customer.of("Peter"
                        ,""
                        ,MailAddress.of("asdsa@web.de")
                        ,HomeAddress.of("Test","Cologne","Westfalen","50937")));
    }

    @Test
    void testCustomerIdPrimitiveEquals(){
        //given
        CustomerId customerId = CustomerId.of(UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d"));
        CustomerId customerId2 = CustomerId.of(UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d"));
        //then
        assertEquals(customerId,customerId2);
    }

    @Test
    void testCustomerIdPrimitiveNotEquals(){
        //given
        CustomerId customerId = CustomerId.of(UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d"));
        CustomerId customerId2 = CustomerId.of(UUID.fromString("28bddc7b-5a4f-460e-a072-63ba90b7cf1d"));
        //then
        assertNotEquals(customerId,customerId2);
    }

    @Test
    void testMailAddressDomainPrimitiveEquals(){
        //given
        MailAddress mailAddress = MailAddress.of("test@test.de");
        MailAddress mailAddress2 = MailAddress.of("test@test.de");
        //then
        assertEquals(mailAddress,mailAddress2);;
    }

    @Test
    void homeAddressDomainPrimitiveEquals(){
        //given
        HomeAddress homeAddress = HomeAddress.of("Teststreet","Cologne","Westfalen","50937");
        HomeAddress homeAddress2 = HomeAddress.of("Teststreet","Cologne","Westfalen","50937");
        //then
        assertEquals(homeAddress,homeAddress2);
    }

    @Test
    void testMailAddressDomainPrimitiveNotEquals(){
        //given
        MailAddress mailAddress = MailAddress.of("test@test.de");
        MailAddress mailAddress2 = MailAddress.of("test@tett.de");
        //then
        assertNotEquals(mailAddress,mailAddress2);
    }

    @Test
    void testHomeAddressDomainPrimitiveNotEquals(){
        //given
        HomeAddress homeAddress = HomeAddress.of("Teststreet","Cologne","Westfalen","50937");
        HomeAddress homeAddress2 = HomeAddress.of("Teststreet","Cologn","Westfalen","50937");
        //then
        assertNotEquals(homeAddress,homeAddress2);
    }


}
