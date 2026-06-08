package customer_service.user.application;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRESTController {

    private CustomUserDetailsService customUserDetailsService;

    public UserRESTController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    //TODO
    // @PostMapping("/users")
    //    public ResponseEntity<RegisterUserDTO> createUser(@RequestBody RegisterUserDTO userDTO) {
    //        customUserDetailsService
    //    }

    //TODO
    // @GetMapping for every user as Admin und um zu gucken ob der Admin user beim start angelegt wurde


}
