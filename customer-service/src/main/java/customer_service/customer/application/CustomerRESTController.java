package customer_service.customer.application;

import customer_service.DTOs.CreateCustomerDTO;
import customer_service.DTOs.CustomerDTO;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.user.domainprimitives.UserId;
import customer_service.customer.domainprimitives.CustomerId;
import customer_service.user.appliaction.CustomUserDetailsService;
import customer_service.user.domain.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class CustomerRESTController {

    private final CustomerApplicationService customerApplicationService;
    private final CustomUserDetailsService customUserDetailsService;

    public CustomerRESTController(CustomerApplicationService customerApplicationService, CustomUserDetailsService customUserDetailsService) {
        this.customerApplicationService = customerApplicationService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOList = customerApplicationService.getAllCustomers();
        return new ResponseEntity<>(customerDTOList, HttpStatus.OK);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID id) {
        CustomerDTO customerDTO = customerApplicationService.getCustomerById(CustomerId.of(id));
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestHeader("Idempotency-Key") UUID idempotencyId, @RequestBody CreateCustomerDTO createCustomerDTO) {
        CustomerDTO createCustomer = customerApplicationService.createCustomer(idempotencyId, createCustomerDTO);
        URI returnURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createCustomer.customerId())
                .toUri();
        return ResponseEntity
                .created(returnURI)
                .body(createCustomer);
    }


    @PatchMapping("/customers/{id}/homeAddress")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateHomeAddressAsAdmin(@PathVariable UUID id, @RequestBody HomeAddressDTO homeAddress) {
        customerApplicationService.changeHomeAddressOfCustomerAsAdmin(id, homeAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/customers/{id}/mailAddress")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateMailAddressAsAdmin(@PathVariable UUID id, @RequestBody MailAddressDTO mailAddressDTO) {
        customerApplicationService.changeMailAddressOfCustomerAsAdmin(id, mailAddressDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/customers/me/mailAddress")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateMailAddress(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MailAddressDTO mailAddress) {
        customerApplicationService.changeMailAddressOfCustomer(userDetails.getUsername(),mailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/customers/me/homeAddress")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateHomeAddress(@AuthenticationPrincipal UserDetails userDetails, @RequestBody HomeAddressDTO homeAddress) {
        customerApplicationService.changeHomeAddressOfCustomer(userDetails.getUsername(), homeAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerApplicationService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
