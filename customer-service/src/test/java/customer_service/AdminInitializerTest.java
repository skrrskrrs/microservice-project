package customer_service;


import customer_service.user.configuration.AdminUserInitializer;
import customer_service.user.domain.UserEntity;
import customer_service.user.domain.UserRepository;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class AdminUserInitializerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminUserInitializer initializer;

    @Test
    void shouldPersistAdminInDatabase() throws Exception {
        CommandLineRunner runner = initializer.init();
        runner.run();

        Optional<UserEntity> admin =
                userRepository.findByUserName(UserNameDomainPrimitive.of("admin"));

        assertTrue(admin.isPresent());
        System.out.println(admin.get().getUserName());
    }

    @Test
    void shouldNotCreateAdminTwice() throws Exception {

        CommandLineRunner runner = initializer.init();
        runner.run();

        runner.run();

        long count = userRepository.count();

        assertEquals(1, count);
    }

}

