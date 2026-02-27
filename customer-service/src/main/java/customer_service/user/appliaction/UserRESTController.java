package customer_service.user.appliaction;

import customer_service.user.DTOs.RegisterUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
