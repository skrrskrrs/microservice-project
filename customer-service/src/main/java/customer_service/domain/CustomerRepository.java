package customer_service.domain;

import customer_service.domainprimitives.CustomerId;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, CustomerId> {
}
