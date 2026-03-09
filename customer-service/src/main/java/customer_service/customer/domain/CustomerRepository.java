package customer_service.customer.domain;

import customer_service.customer.domainprimitives.CustomerId;
import customer_service.user.domainprimitives.UserId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, CustomerId> {
    Optional<Customer> findCustomerByUserId(UserId userId);
}
