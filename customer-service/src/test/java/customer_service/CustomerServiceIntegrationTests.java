package customer_service;

import customer_service.DTOs.CustomerDTO;
import customer_service.customer.application.CustomerApplicationService;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerException;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;
import customer_service.idempotency.application.IdempotencyApplicationService;
import customer_service.idempotency.domain.IdempotencyException;
import customer_service.idempotency.domain.IdempotencyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@Transactional
public class CustomerServiceIntegrationTests {
    private Customer validCustomer;
    private CustomerDTO validCustomerDTO;
    private final CustomerRepository customerRepository;
    private final CustomerApplicationService customerApplicationService;
    private final IdempotencyApplicationService idempotencyApplicationService;

    @Autowired
    CustomerServiceIntegrationTests(CustomerRepository customerRepository, CustomerApplicationService customerApplicationService, IdempotencyApplicationService idempotencyApplicationService) {
        this.customerRepository = customerRepository;
        this.customerApplicationService = customerApplicationService;
        this.idempotencyApplicationService = idempotencyApplicationService;
    }

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true"); // zum Debuggen
    }

    @BeforeEach
    void setUp() {
        validCustomer = Customer.of("Peter",
                "Meier",
                MailAddress.of("test@valid.de"),
                HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937"));
        customerRepository.save(validCustomer);

    }

    @Test
    void findCustomer() {
        //given
        Customer customer = customerRepository
                .findById(validCustomer.getCustomerId())
                .orElseThrow(() ->  new CustomerException("Customer does not exist"));
        //when
        Optional<Customer> found = customerRepository.findById(customer.getCustomerId());
        //then
        assertTrue(found.isPresent());
    }

    @Test
    void changeHomeAddressOfCustomer() {
        //given
        HomeAddressDTO homeAddressDTO = new HomeAddressDTO("NewAddress","Munich","Dunno","50939");
        customerApplicationService.changeHomeAddressOfCustomer(validCustomer.getCustomerId(), homeAddressDTO);
        //when
        Customer reloaded = customerRepository
                .findById(validCustomer.getCustomerId())
                .orElseThrow(() -> new CustomerException("Customer does not exist"));
        //then
        HomeAddress expected = HomeAddress.of("NewAddress","Munich","Dunno","50939");
        assertEquals(expected, reloaded.getHomeAddress());
    }

    @Test
    void changeMailAddressOfCustomer() {
        //given
        MailAddressDTO mailAddressDTO = new MailAddressDTO("newMail@web.de");
        customerApplicationService.changeMailAddressOfCustomer(validCustomer.getCustomerId(), mailAddressDTO);
        //when
        Customer reload = customerRepository
                .findById(validCustomer.getCustomerId())
                .orElseThrow(() -> new CustomerException("Customer does not exist"));
        //then
        MailAddress expected = MailAddress.of("newMail@web.de");
        assertEquals(expected, reload.getMailAddress());
    }


    @Test
    void doubleIdempotencyKeyCheck() {
        //given
        UUID key = UUID.fromString("ebb4b70f-8f33-41c3-816e-ec84373ddbe3");
        //when
        idempotencyApplicationService.ensureIdempotencyOnce(key);
        //then
        assertThrows(IdempotencyException.class,
                () -> idempotencyApplicationService.ensureIdempotencyOnce(key)
        );
    }

}
