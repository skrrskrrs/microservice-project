package org.example.customerservice.customer.application;

import org.example.customerservice.customer.domain.CustomerException;
import org.example.customerservice.customer.domain.CustomerRepository;
import org.example.customerservice.customer.domainprimitives.CustomerId;
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
    private final CustomerRepository customerRepository;

    public CustomerRESTController(CustomerApplicationService customerApplicationService, CustomerRepository customerRepository) {
        this.customerApplicationService = customerApplicationService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers/{id}/exists")
    public ResponseEntity<Void> exists(@PathVariable UUID id) {
        return customerRepository.existsById(CustomerId.newInstance(id))
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOList = customerApplicationService.getAllCustomers();
        return new ResponseEntity<>(customerDTOList, HttpStatus.OK);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID id) {
        CustomerDTO customerDTO = customerApplicationService.getCustomerById(CustomerId.newInstance(id));
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @PostMapping("customers")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO createCustomer = customerApplicationService.createCustomer(customerDTO);
        URI returnURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand( createCustomer.customerId())
                .toUri();
        return ResponseEntity
                .created(returnURI)
                .body( createCustomer);
    }

    @PatchMapping("/customers/{id}/mailAddress")
    public ResponseEntity<Void> updateMailAddress(@PathVariable UUID id, @RequestBody MailAddressDTO mailAddress) {
        customerApplicationService.changeMailAddressOfCustomer(CustomerId.newInstance(id),mailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/customers/{id}/homeAddress")
    public ResponseEntity<Void> updateHomeAddress(@PathVariable UUID id, @RequestBody HomeAddressDTO homeAddress) {
        customerApplicationService.changeHomeAddressOfCustomer(CustomerId.newInstance(id),homeAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerApplicationService.deleteCustomer(CustomerId.newInstance(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
