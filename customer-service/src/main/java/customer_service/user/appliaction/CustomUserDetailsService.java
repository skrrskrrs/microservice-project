package customer_service.user.appliaction;

import customer_service.user.domain.Role;
import customer_service.user.domain.UserEntity;
import customer_service.user.domain.UserRepository;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Suche User in PostgreSQL
        UserEntity user = userRepository.findByUserName(UserNameDomainPrimitive.of(username)).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        // Konvertiere zu Spring Security UserDetails
        return User.builder()
                .username(user.getUserName().getUserName())
                .password(user.getHashedPassword().getHashedPassword())  // Bereits verschlüsselt in DB!
                .authorities(getAuthorities(user.getRoles()))
                .disabled(!user.isEnabled())
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
}
