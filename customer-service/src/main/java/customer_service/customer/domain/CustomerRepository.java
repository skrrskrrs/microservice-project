package customer_service.customer.domain;

import customer_service.customer.domainprimitives.CustomerId;
import customer_service.user.domainprimitives.UserId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, CustomerId> {
    Optional<Customer> findCustomerByUserId(UserId userId);
}
