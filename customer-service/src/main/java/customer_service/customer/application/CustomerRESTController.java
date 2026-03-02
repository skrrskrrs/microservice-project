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

    // TODO Admin darf kunden löschen, Customer darf nur /kunden/me sehen  mit Authentication
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

    @PostMapping("customers")
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

    @PatchMapping("/customers/{id}/mailAddress")
    @PreAuthorize("hasRole('ADMIN') or (#id.toString() == principal.username)")
    public ResponseEntity<CustomerDTO> updateMailAddress(@PathVariable UUID id, @RequestBody MailAddressDTO mailAddress) {
        customerApplicationService.changeMailAddressOfCustomer(UserId.of(id), mailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/customers/{id}/homeAddress")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> updateHomeAddress(@PathVariable UUID id, @RequestBody HomeAddressDTO homeAddress) {
        customerApplicationService.changeHomeAddressOfCustomer(UserId.of(id), homeAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/customers/me/mailAddress")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateMailAddress(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MailAddressDTO mailAddress) {
        customerApplicationService.changeMailAddressOfCustomer(userDetails.getUsername(),mailAddress);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/customers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerApplicationService.deleteCustomer(CustomerId.of(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
