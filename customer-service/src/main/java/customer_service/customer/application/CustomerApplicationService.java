package customer_service.customer.application;

import customer_service.idempotency.application.IdempotencyApplicationService;
import customer_service.idempotency.domain.Idempotency;
import customer_service.idempotency.domain.IdempotencyRepository;
import jakarta.transaction.Transactional;
import customer_service.DTOs.CustomerDTO;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.customer.domain.Customer;
import customer_service.customer.domain.CustomerException;
import customer_service.customer.domainprimitives.CustomerId;
import customer_service.customer.domain.CustomerRepository;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerApplicationService {

    private final CustomerRepository customerRepository;
    private final IdempotencyApplicationService idempotencyApplicationService;

    public CustomerApplicationService(CustomerRepository customerRepository, IdempotencyApplicationService idempotencyApplicationService) {
        this.customerRepository = customerRepository;
        this.idempotencyApplicationService = idempotencyApplicationService;
    }

    public List<CustomerDTO> getAllCustomers() {
        Iterable<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for ( Customer customer : customers){
            CustomerDTO customerDTO = new CustomerDTO(
                    customer.getCustomerId().getId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getMailAddress().getMailAddress(),
                    customer.getHomeAddress().getStreet(),
                    customer.getHomeAddress().getCity(),
                    customer.getHomeAddress().getState(),
                    customer.getHomeAddress().getZip()
            );
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }

    public Customer findCustomerById(CustomerId customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomerException("Customer does not exist"));
    }

    public CustomerDTO getCustomerById(CustomerId customerId) {
        Customer customer = findCustomerById(customerId);
        return new CustomerDTO(customer.getCustomerId().getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getMailAddress().getMailAddress(),
                customer.getHomeAddress().getStreet(),
                customer.getHomeAddress().getCity(),
                customer.getHomeAddress().getState(),
                customer.getHomeAddress().getZip()
        );
    }

    public CustomerDTO createCustomer(UUID idempotencyKey, CustomerDTO customerDTO) {
        idempotencyApplicationService.ensureIdempotencyOnce(idempotencyKey);
        Customer customer = Customer.newInstance(
                customerDTO.firstName(),
                customerDTO.lastName(),
                MailAddress.newInstance(customerDTO.mailAddress()),
                HomeAddress.newInstance(customerDTO.street(),customerDTO.city(),customerDTO.state(),customerDTO.zip())
        );
        customerRepository.save(customer);

        return new CustomerDTO(customer.getCustomerId().getId(),customer.getFirstName(),customer.getLastName(),customer.getMailAddress().getMailAddress(),customer.getHomeAddress().getStreet(), customerDTO.city(), customerDTO.state(), customerDTO.zip());
    }

    @Transactional
    public void changeMailAddressOfCustomer(CustomerId customerId, MailAddressDTO mailAddress) {
        Customer customer = findCustomerById(customerId);
        MailAddress updatedMailAddress = MailAddress.newInstance(mailAddress.mailAddress());
        customer.changeMailAddress(updatedMailAddress);
    }

    @Transactional
    public void changeHomeAddressOfCustomer(CustomerId customerId, HomeAddressDTO homeAddress) {
        Customer customer = findCustomerById(customerId);
        HomeAddress newHomeAddress = HomeAddress.newInstance(homeAddress.street(),homeAddress.city(),homeAddress.state(),homeAddress.zip());
        customer.changeHomeAddress(newHomeAddress);
    }

    public void deleteCustomer(CustomerId customerId) {
        customerRepository.deleteById(customerId);
    }

}
