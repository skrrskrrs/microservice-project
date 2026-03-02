package customer_service;

import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerException;
import customer_service.customer.domainprimitives.CustomerId;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;
import customer_service.customer.domainprimitives.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;


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
                        ,HomeAddress.of("Teststreet","Cologne","Westfalen","50937"),
                        UserId.of(UUID.fromString(""))));

        assertThrows(CustomerException.class,
                () -> Customer.of("Eeat"
                        ,"Test"
                        ,MailAddress.of("asdsa@web.de")
                        ,HomeAddress.of(null,null,null,null),
                        null));

        assertThrows(CustomerException.class,
                () -> Customer.of("Eeat"
                        ,"Test"
                        ,MailAddress.of("asdsa@web.de")
                        ,HomeAddress.of("","Cologne","Westfalen","50937"),
                        UserId.of(UUID.fromString("c4dab129-e51c-4406-9376-ca2d81bc2640"))));



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
