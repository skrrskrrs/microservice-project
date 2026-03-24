package customer_service;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.PercentCodec;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.CustomerId;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;
import customer_service.user.domain.Role;
import customer_service.user.domain.UserEntity;
import customer_service.user.domain.UserRepository;
import customer_service.user.domainprimitives.HashedPasswordDomainPrimitive;
import customer_service.user.domainprimitives.UserId;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test-container")
class CustomerControllerRestAssuredTest {

    @LocalServerPort
    int port;

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private UserEntity validUserEntityTestUser, validAdminEntity, validUserEntityOtherUser;
    private Customer validCustomer, validCustomerWithId;
    private PasswordEncoder passwordEncoder;

    @Autowired
    CustomerControllerRestAssuredTest(CustomerRepository customerRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");

    @BeforeEach
    public void setup() {
        String adminPassword = passwordEncoder.encode("Test@2026");
        validAdminEntity = UserEntity.createWithRoles(UserNameDomainPrimitive.of("Admin")
                , HashedPasswordDomainPrimitive.of(adminPassword), Set.of(Role.ROLE_ADMIN));
        userRepository.save(validAdminEntity);

        validCustomerWithId = Customer.withId(CustomerId.of(UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d"))
                , "Peter",
                "Meier",
                MailAddress.of("test@valid.de"),
                HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937"),
                UserId.of(validAdminEntity.getId().getId()));
        customerRepository.save(validCustomerWithId);
    }

    @Test
    void updateMailAddressViaRESTAsAdmin() throws Exception {
        userRepository.findAll().forEach(u ->
                System.out.println("DB USER: " + u.getUserName().getUserName())
        );
        // given
        MailAddressDTO mailAddressDTO = new MailAddressDTO("newMail@web.de");
        UUID testId = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");

        // when + then
        given()
                .port(port)
                .contentType("application/json")
                .body(mailAddressDTO)
                .auth().basic("Admin", "Test@2026") // wichtig statt @WithMockUser
                .when()
                .patch("/customers/{id}/mailAddress", testId)
                .then()
                .statusCode(200);

        Customer updated = customerRepository.findById(CustomerId.of(testId))
                .orElseThrow(() -> new AssertionError("Customer not found"));

        assertEquals("newMail@web.de", updated.getMailAddress().getMailAddress());
    }

}