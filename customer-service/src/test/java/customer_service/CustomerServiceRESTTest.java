package customer_service;


import customer_service.DTOs.CreateCustomerDTO;
import customer_service.DTOs.CustomerDTO;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class CustomerServiceRESTTest {
    private final CustomerRepository customerRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public CustomerServiceRESTTest(CustomerRepository customerRepository, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
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

    @Test
    public void addAndGetCustomerViaREST() throws Exception {
        //given
        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("Hans",
                "Meier",
                MailAddressDTO.mailAddressAsDTO(MailAddress.of("test@web.de")),
                HomeAddressDTO.homeAddressAsDTO(HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937"))
        );

        String json = objectMapper.writeValueAsString(createCustomerDTO);

        String idempotencyKey = UUID.randomUUID().toString();

        //when
        MvcResult mvcResult = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("idempotency-key", idempotencyKey)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(createCustomerDTO.firstName())))
                .andExpect(jsonPath("$.lastName", is(createCustomerDTO.lastName())))
                .andExpect(jsonPath("$.mailAddressDTO.mailAddress", is(createCustomerDTO.mailAddressDTO().mailAddress())))
                .andExpect(jsonPath("$.homeAddressDTO.city", is(createCustomerDTO.homeAddressDTO().city())))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");

        //then
        mockMvc.perform(get(location))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(createCustomerDTO.firstName())))
                .andExpect(jsonPath("$.lastName", is(createCustomerDTO.lastName())))
                .andExpect(jsonPath("$.mailAddressDTO.mailAddress", is(createCustomerDTO.mailAddressDTO().mailAddress())))
                .andExpect(jsonPath("$.homeAddressDTO.city", is(createCustomerDTO.homeAddressDTO().city())));
    }

    @Test
    public void testDoubleIdempotencyKey() throws Exception {
        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("Hans",
                "Meier",
                MailAddressDTO.mailAddressAsDTO(MailAddress.of("test@web.de")),
                HomeAddressDTO.homeAddressAsDTO(HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937"))
        );

        String json = objectMapper.writeValueAsString(createCustomerDTO);
        String idempotencyKey = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d").toString();

        MvcResult mvcResult = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Idempotency-Key", idempotencyKey)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult mvcResult2 = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Idempotency-Key", idempotencyKey)
                        .content(json))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void testGet404ForUnknownID() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/customers/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
