package customer_service.customer.application;

import customer_service.DTOs.CreateCustomerDTO;
import customer_service.user.domain.UserEntity;

public interface UserDetailsServiceCustomer {
    UserEntity createUser(CreateCustomerDTO createCustomerDTO);
    UserEntity findUserByName(String userName);
}
