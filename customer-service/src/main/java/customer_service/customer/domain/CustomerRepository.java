package customer_service.customer.domain;

import customer_service.customer.domainprimitives.CustomerId;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, CustomerId> {

}
