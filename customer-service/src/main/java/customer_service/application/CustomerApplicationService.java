package customer_service.application;

import jakarta.transaction.Transactional;
import customer_service.DTOs.CustomerDTO;
import customer_service.DTOs.HomeAddressDTO;
import customer_service.DTOs.MailAddressDTO;
import customer_service.domain.Customer;
import customer_service.domain.CustomerException;
import customer_service.domainprimitives.CustomerId;
import customer_service.domain.CustomerRepository;
import customer_service.domainprimitives.HomeAddress;
import customer_service.domainprimitives.MailAddress;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerApplicationService {

    private final CustomerRepository customerRepository;

    public CustomerApplicationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
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
