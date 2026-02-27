package customer_service.user.DTOs;

import customer_service.user.domain.Role;

import java.util.Set;

public record CreateUserRequest(
        String userName,
        String password,
        Set<Role> roles,
        boolean enabled
) {}