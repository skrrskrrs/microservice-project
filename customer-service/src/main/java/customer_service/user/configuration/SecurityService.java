package customer_service.user.configuration;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityService {

    public boolean isSelf(UUID id){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return currentUser.equals(id.toString());
    }
}
