package customer_service.user.configuration;

import customer_service.user.domain.Role;
import customer_service.user.domain.UserEntity;
import customer_service.user.domain.UserRepository;
import customer_service.user.domainprimitives.HashedPasswordDomainPrimitive;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
@Profile("!test-container")
public class AdminUserInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
                if(userRepository.findByUserName(UserNameDomainPrimitive.of("admin")).isEmpty()) {
                String hashedPassword = passwordEncoder.encode("Admin123!");
                UserEntity admin = UserEntity.createWithRoles(UserNameDomainPrimitive.of("admin"), HashedPasswordDomainPrimitive.of(hashedPassword), Set.of(Role.ROLE_ADMIN));

                userRepository.save(admin);
            }
        };
    }
}
