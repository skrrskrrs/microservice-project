package customer_service;

import customer_service.customer.application.CustomerApplicationService;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerException;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
public class CustomerServiceIntegrationTests {
    private Customer validCustomer;
    private final CustomerRepository customerRepository;
    private final CustomerApplicationService customerApplicationService;

    @Autowired
    CustomerServiceIntegrationTests(CustomerRepository customerRepository, CustomerApplicationService customerApplicationService) {
        this.customerRepository = customerRepository;
        this.customerApplicationService = customerApplicationService;
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
    }

    @BeforeEach
    void setUp() {
        validCustomer = Customer.newInstance("Peter",
                "Meier",
                MailAddress.newInstance("test@valid.de"),
                HomeAddress.newInstance("Teststreet","Cologne","Westfalen","50937"));
        customerRepository.save(validCustomer);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void findCustomer() {
        Customer customer = customerRepository
                .findById(validCustomer
                        .getCustomerId()).orElseThrow(() ->  new CustomerException("Customer does not exist"));

        Optional<Customer> found = customerRepository.findById(customer.getCustomerId());
        assertTrue(found.isPresent());
    }

    @Test
    void changeHomeAddressOfCustomer() {
        HomeAddressDTO homeAddressDTO = new HomeAddressDTO("NewAddress","Munich","Dunno","50939");
        customerApplicationService.changeHomeAddressOfCustomer(validCustomer.getCustomerId(), homeAddressDTO);

        Customer reloaded = customerRepository
                .findById(validCustomer.getCustomerId())
                .orElseThrow(() -> new CustomerException("Customer does not exist"));

        HomeAddress expected = HomeAddress.newInstance("NewAddress","Munich","Dunno","50939");
        assertEquals(expected, reloaded.getHomeAddress());
    }

    @Test
    void changeMailAddressOfCustomer() {
        MailAddressDTO mailAddressDTO = new MailAddressDTO("newMail@web.de");
        customerApplicationService.changeMailAddressOfCustomer(validCustomer.getCustomerId(), mailAddressDTO);

        Customer reload = customerRepository.findById(validCustomer.getCustomerId()).orElseThrow(() -> new CustomerException("Customer does not exist"));

        MailAddress expected = MailAddress.newInstance("newMail@web.de");
        assertEquals(expected, reload.getMailAddress());
    }
}
