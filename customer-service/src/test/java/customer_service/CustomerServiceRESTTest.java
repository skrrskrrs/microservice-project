package customer_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import customer_service.DTOs.CreateCustomerDTO;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.CustomerId;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;

import customer_service.user.appliaction.CustomUserDetailsService;
import customer_service.user.domain.Role;
import customer_service.user.domain.UserEntity;
import customer_service.user.domain.UserRepository;
import customer_service.user.domainprimitives.HashedPasswordDomainPrimitive;
import customer_service.user.domainprimitives.UserId;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;
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
@Transactional
@ActiveProfiles("test-container")
public class CustomerServiceRESTTest {
    private final CustomerRepository customerRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private UserEntity validUserEntity, validAdminEntity;
    private Customer validCustomer, validCustomerWithId;
    private final UserRepository userRepository;

    @Autowired
    public CustomerServiceRESTTest(CustomerRepository customerRepository, MockMvc mockMvc, ObjectMapper objectMapper, CustomUserDetailsService customUserDetailsService, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");


    @BeforeEach
    public void setup() {
        validUserEntity = UserEntity.registerNewUser(UserNameDomainPrimitive.of("TestUser")
                , HashedPasswordDomainPrimitive.of("Test@2026"));
        userRepository.save(validUserEntity);

        validAdminEntity = UserEntity.createWithRoles(UserNameDomainPrimitive.of("Admin")
                , HashedPasswordDomainPrimitive.of("Test@2026"), Set.of(Role.ROLE_ADMIN));
        userRepository.save(validAdminEntity);


        validCustomer = Customer.of("Peter",
                "Meier",
                MailAddress.of("test@valid.de"),
                HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937"),
                UserId.of(validUserEntity.getId().getId()));
        customerRepository.save(validCustomer);

        validCustomerWithId = Customer.withId(CustomerId.of(UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d"))
                ,"Peter",
                "Meier",
                MailAddress.of("test@valid.de"),
                HomeAddress.of("Teststreet", "Cologne", "Westfalen", "50937"),
                UserId.of(validAdminEntity.getId().getId()));
        customerRepository.save(validCustomerWithId);
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
        assertNotNull(location);
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
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateMailAddressViaRESTAsAdmin() throws Exception {
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
        Customer updated = customerRepository.findCustomerByUserId(validAdminEntity.getId())
                .orElseThrow(() -> new AssertionError("Customer not found in DB"));
        assertEquals(updated.getMailAddress(), MailAddress.of("newMail@web.de"));
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"USER"})
    public void updateMailAddressViaRESTAsUser() throws Exception {
        //given
        MailAddressDTO mailAddressDTO = new MailAddressDTO("newMail@web.de");
        UUID testId = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");
        String patchJson = objectMapper.writeValueAsString(mailAddressDTO);

        //when
        mockMvc.perform(patch("/customers/{id}/mailAddress",
                        testId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void updateMailAddressViaRESTAsUnknown() throws Exception {
        //given
        MailAddressDTO mailAddressDTO = new MailAddressDTO("newMail@web.de");
        UUID testId = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");
        String patchJson = objectMapper.writeValueAsString(mailAddressDTO);

        //when
        mockMvc.perform(patch("/customers/{id}/mailAddress",
                        testId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateHomeAddressAsAdmin() throws Exception {
        //given
        HomeAddressDTO dto = new HomeAddressDTO("New Street", "New City", "Cologne","50886");
        UUID testId = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");
        String patchJson = objectMapper.writeValueAsString(dto);

        //when
        mockMvc.perform(patch("/customers/{id}/homeAddress", testId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk());

        //then
        Customer customer = customerRepository.findById(CustomerId.of(testId)).orElseThrow(() -> new AssertionError("Customer not found in DB"));
        assertEquals(customer.getHomeAddress(),HomeAddress.of("New Street", "New City", "Cologne","50886"));
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"USER"})
    void testUpdateHomeAddressAsUser() throws Exception {
        //given
        HomeAddressDTO dto = new HomeAddressDTO("New Street", "New City", "Cologne","50886");
        String patchJson = objectMapper.writeValueAsString(dto);

        UserEntity user = customUserDetailsService.findUserByName("TestUser");

        //when
        mockMvc.perform(patch("/customers/me/homeAddress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk());

        // then
        Customer updated = customerRepository.findCustomerByUserId(user.getId())
                .orElseThrow(() -> new AssertionError("Customer not found in DB"));
        assertEquals(updated.getHomeAddress(), HomeAddress.of("New Street", "New City", "Cologne", "50886"));
    }

    @Test
    public void updateHomeAddressAsUnknown() throws Exception {
        //given
        HomeAddressDTO dto = new HomeAddressDTO("New Street", "New City", "Cologne","50886");
        UUID testId = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");
        String patchJson = objectMapper.writeValueAsString(dto);

        //when
        mockMvc.perform(patch("/customers/{id}/homeAddress",
                        testId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
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
    @WithMockUser(username = "user", roles = {"USER"})
    public void deleteCustomerViaRESTAsUser() throws Exception {
        //given
        UUID testId = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");

        //when & then
        mockMvc.perform(delete("/customers/{id}",
                        testId.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteCustomerViaRESTAsUnknown() throws Exception {
        //given
        UUID testId = UUID.fromString("27bddc7b-5a4f-460e-a072-63ba90b7cf1d");

        //when & then
        mockMvc.perform(delete("/customers/{id}",
                        testId.toString()))
                .andExpect(status().isUnauthorized());
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
