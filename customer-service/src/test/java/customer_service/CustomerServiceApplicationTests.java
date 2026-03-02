package customer_service;

import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerException;
import customer_service.customer.domainprimitives.CustomerId;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;
import customer_service.user.domain.UserEntity;
import customer_service.user.domain.UserException;
import customer_service.user.domainprimitives.HashedPasswordDomainPrimitive;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
                        , "Test"
                        , MailAddress.of("asdsad.de")
                        , HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937"),
                        UserId.of(UUID.fromString(""))));

        assertThrows(CustomerException.class,
                () -> Customer.of("Eeat"
                        , "Test"
                        , MailAddress.of("asdsa@web.de")
                        , HomeAddress.of(null, null, null, null),
                        null));

        assertThrows(CustomerException.class,
                () -> Customer.of("Eeat"
                        , "Test"
                        , MailAddress.of("asdsa@web.de")
                        , HomeAddress.of("", "Cologne", "Westfalen", "50937"),
                        UserId.of(UUID.randomUUID())));

        assertThrows(CustomerException.class,
                () -> Customer.of(""
                        , "Test"
                        , MailAddress.of("asdsa@web.de")
                        , HomeAddress.of("Test", "Cologne", "Westfalen", "50937"),
                        UserId.of(UUID.randomUUID())));

        assertThrows(CustomerException.class,
                () -> Customer.of("Peter"
                        , ""
                        , MailAddress.of("asdsa@web.de")
                        , HomeAddress.of("Test", "Cologne", "Westfalen", "50937"),
                        UserId.of(UUID.randomUUID())));
    }

    @Test
    void shouldFailBecauseNoExceptionThrown() {
        assertThrows(UserException.class,
                () -> {
                    UserNameDomainPrimitive.of("Tes");
                }
        );

        assertThrows(UserException.class,
                () -> {
                    HashedPasswordDomainPrimitive.of("Sec!");
                }
        );

    }

    @Test
    void createInvalidUser() {

        assertThrows(UserException.class,
                () -> UserEntity.createWithRoles(UserNameDomainPrimitive.of("")
                        , HashedPasswordDomainPrimitive.of("Secure1!")
                        ,null));

        assertThrows(UserException.class,
                () -> UserEntity.registerNewUser(UserNameDomainPrimitive.of("Test")
                        , HashedPasswordDomainPrimitive.of(null)));

        assertThrows(UserException.class,
                () -> UserEntity.registerNewUser(UserNameDomainPrimitive.of("Testasdasd")
                        , HashedPasswordDomainPrimitive.of("Secure1!")));

        assertThrows(UserException.class,
                () -> UserEntity.registerNewUser(UserNameDomainPrimitive.of("Testtss")
                        , HashedPasswordDomainPrimitive.of("Secu!")));


        assertThrows(UserException.class,
                () -> UserEntity.registerNewUser(UserNameDomainPrimitive.of("")
                        , HashedPasswordDomainPrimitive.of("Secured1!")));

        assertThrows(UserException.class,
                () -> UserEntity.registerNewUser(UserNameDomainPrimitive.of("xPeterX")
                        , HashedPasswordDomainPrimitive.of("")));

        assertThrows(UserException.class,
                () -> UserEntity.registerNewUser(UserNameDomainPrimitive.of(null)
                        , HashedPasswordDomainPrimitive.of("Secured1!")));
    }

    @Test
    void testHashedPasswordPrimitiveIsEquals() {
        //given
        HashedPasswordDomainPrimitive hashedPasswordPrimitive = HashedPasswordDomainPrimitive.of("Secure1!");
        HashedPasswordDomainPrimitive hashedPasswordPrimitive2 = HashedPasswordDomainPrimitive.of("Secure1!");
        //then
        assertEquals(hashedPasswordPrimitive, hashedPasswordPrimitive2);
    }

    @Test
    void testHashedPasswordPrimitiveIsNotEquals() {
        //given
        HashedPasswordDomainPrimitive hashedPasswordPrimitive = HashedPasswordDomainPrimitive.of("Secure1!");
        HashedPasswordDomainPrimitive hashedPasswordPrimitive2 = HashedPasswordDomainPrimitive.of("Secure2!");
        //then
        assertNotEquals(hashedPasswordPrimitive, hashedPasswordPrimitive2);
    }

    @Test
    void testCustomerIdPrimitiveEquals() {
        //given
        CustomerId customerId = CustomerId.of(UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d"));
        CustomerId customerId2 = CustomerId.of(UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d"));
        //then
        assertEquals(customerId, customerId2);
    }

    @Test
    void testCustomerIdPrimitiveNotEquals() {
        //given
        CustomerId customerId = CustomerId.of(UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d"));
        CustomerId customerId2 = CustomerId.of(UUID.fromString("28bddc7b-5a4f-460e-a072-63ba90b7cf1d"));
        //then
        assertNotEquals(customerId, customerId2);
    }

    @Test
    void testMailAddressDomainPrimitiveEquals() {
        //given
        MailAddress mailAddress = MailAddress.of("test@test.de");
        MailAddress mailAddress2 = MailAddress.of("test@test.de");
        //then
        assertEquals(mailAddress, mailAddress2);
        ;
    }

    @Test
    void homeAddressDomainPrimitiveEquals() {
        //given
        HomeAddress homeAddress = HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937");
        HomeAddress homeAddress2 = HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937");
        //then
        assertEquals(homeAddress, homeAddress2);
    }

    @Test
    void testMailAddressDomainPrimitiveNotEquals() {
        //given
        MailAddress mailAddress = MailAddress.of("test@test.de");
        MailAddress mailAddress2 = MailAddress.of("test@tett.de");
        //then
        assertNotEquals(mailAddress, mailAddress2);
    }

    @Test
    void testHomeAddressDomainPrimitiveNotEquals() {
        //given
        HomeAddress homeAddress = HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937");
        HomeAddress homeAddress2 = HomeAddress.of("Teststreet", "Cologn", "Westfalen", "50937");
        //then
        assertNotEquals(homeAddress, homeAddress2);
    }


}
