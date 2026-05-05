package customer_service.customer.domain;

import customer_service.customer.domainprimitives.CustomerId;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.user.domainprimitives.UserId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, CustomerId> {
    Optional<Customer> findCustomerByUserId(UserId userId);

    @Modifying
    @Query("""
    UPDATE Customer c
    SET c.homeAddress = :address
    WHERE c.userId = :userId
""")
    void updateHomeAddress(@Param("userId") Long userId,
                           @Param("address") HomeAddress address);
}
