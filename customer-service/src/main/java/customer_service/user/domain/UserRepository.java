package customer_service.user.domain;

import customer_service.user.domainprimitives.UserId;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UserId> {
    Optional <UserEntity> findByUserName(UserNameDomainPrimitive userName);
}
