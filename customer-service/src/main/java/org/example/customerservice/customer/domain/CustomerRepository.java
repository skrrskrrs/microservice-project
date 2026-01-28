package org.example.customerservice.customer.domain;

import org.example.customerservice.customer.domainprimitives.CustomerId;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, CustomerId> {
}
