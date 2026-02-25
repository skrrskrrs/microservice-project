package customer_service.customer.application;

import customer_service.DTOs.CustomerDTO;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.CustomerId;
import customer_service.idempotency.application.IdempotencyApplicationService;
import customer_service.idempotency.domain.Idempotency;
import customer_service.idempotency.domain.IdempotencyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class CustomerRESTController {

    private final CustomerApplicationService customerApplicationService;

    public CustomerRESTController(CustomerApplicationService customerApplicationService) {
        this.customerApplicationService = customerApplicationService;
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

    @PostMapping("customers")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestHeader("Idempotency-Key") UUID idempotencyId, @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createCustomer = customerApplicationService.createCustomer(idempotencyId, customerDTO);
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
    public ResponseEntity<CustomerDTO> updateMailAddress(@PathVariable UUID id, @RequestBody MailAddressDTO mailAddress) {
        customerApplicationService.changeMailAddressOfCustomer(CustomerId.of(id), mailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/customers/{id}/homeAddress")
    public ResponseEntity<CustomerDTO> updateHomeAddress(@PathVariable UUID id, @RequestBody HomeAddressDTO homeAddress) {
        customerApplicationService.changeHomeAddressOfCustomer(CustomerId.of(id), homeAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerApplicationService.deleteCustomer(CustomerId.of(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
