package customer_service;

import customer_service.DTOs.CreateCustomerDTO;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.CustomerId;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test-container")
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
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");


    @BeforeEach
    public void setup() {
        Customer customer = Customer.withId(
                CustomerId.of(UUID.fromString(("27bddc7b-5a4f-460e-a072-63ba90b7cf1d"))),
                "Hans",
                "Meier",
                MailAddress.of("old@mail.de"),
                HomeAddress.of("Street", "City", "State", "12345")
        );
        customerRepository.save(customer);
    }

    @Test
    public void addAndGetCustomerViaREST() throws Exception {
        //given
        CreateCustomerDTO testDTO = SampleData.customerDTOHans();

        String json = objectMapper.writeValueAsString(testDTO);
        String idempotencyKey = UUID.randomUUID().toString();

        //when
        MvcResult mvcResult = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("idempotency-key", idempotencyKey)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(testDTO.firstName())))
                .andExpect(jsonPath("$.lastName", is(testDTO.lastName())))
                .andExpect(jsonPath("$.mailAddressDTO.mailAddress", is(testDTO.mailAddressDTO().mailAddress())))
                .andExpect(jsonPath("$.homeAddressDTO.city", is(testDTO.homeAddressDTO().city())))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");

        //then
        mockMvc.perform(get(location))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(testDTO.firstName())))
                .andExpect(jsonPath("$.lastName", is(testDTO.lastName())))
                .andExpect(jsonPath("$.mailAddressDTO.mailAddress", is(testDTO.mailAddressDTO().mailAddress())))
                .andExpect(jsonPath("$.homeAddressDTO.city", is(testDTO.homeAddressDTO().city())));
    }

    @Test
    public void testDoubleIdempotencyKey() throws Exception {
        //given
        CreateCustomerDTO testDTO = SampleData.customerDTOHans();

        String json = objectMapper.writeValueAsString(testDTO);
        UUID idempotencyKey = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");

        //when
        MvcResult mvcResult = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Idempotency-Key", idempotencyKey)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Idempotency-Key", idempotencyKey)
                        .content(json))
                .andExpect(status().isConflict())
                .andDo(print())
                .andReturn();

        //then
        mockMvc.perform(get(location))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateMailAddressViaREST() throws Exception {
        //given
        MailAddressDTO mailAddressDTO = new MailAddressDTO("newMail@web.de");
        UUID testId = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");
        String patchJson = objectMapper.writeValueAsString(mailAddressDTO);

        //when
        mockMvc.perform(patch("/customers/{id}/mailAddress",
                        testId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Customer updated = customerRepository.findById(CustomerId.of(testId)).orElseThrow(() -> new AssertionError("Customer not found in DB"));
        assertEquals(updated.getMailAddress().getMailAddress(), mailAddressDTO.mailAddress());

    }

    @Test
    public void deleteCustomerViaREST() throws Exception {
        //given
        UUID testId = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");

        //when
        mockMvc.perform(delete("/customers/{id}",
                        testId.toString()))
                .andExpect(status().isNoContent());
        //then
        boolean exist = customerRepository.existsById(CustomerId.of(testId));
        assertFalse(exist);
    }

    @Test
    public void testGet404ForUnknownID() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/customers/{id}",
                        UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
