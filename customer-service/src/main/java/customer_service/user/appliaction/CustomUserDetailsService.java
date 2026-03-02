package customer_service.user.appliaction;

import customer_service.DTOs.CreateCustomerDTO;
import customer_service.user.domain.Role;
import customer_service.user.domain.UserEntity;
import customer_service.user.domain.UserException;
import customer_service.user.domain.UserRepository;
import customer_service.user.domainprimitives.HashedPasswordDomainPrimitive;
import customer_service.user.domainprimitives.UserId;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public UserEntity createUser(CreateCustomerDTO createCustomerDTO) {
        String encodedPassword = passwordEncoder.encode(createCustomerDTO.password());
        UserEntity user = UserEntity.registerNewUser(UserNameDomainPrimitive.of(createCustomerDTO.userName()),HashedPasswordDomainPrimitive.of(encodedPassword));
        userRepository.save(user);
        return user;
    }

    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    public UserEntity findUserByName(String userName){
        return userRepository.findByUserName(UserNameDomainPrimitive.of(userName)).orElseThrow(() -> new UserException("USer does not exist"));
    }



    //TODO
    // public RegisterUserDTO registerUserDTO(RegisterUserDTO userDTO) {
    //        UserEntity user = UserEntity.registerNewUser(UserNameDomainPrimitive.of(userDTO.userName()), HashedPasswordDomainPrimitive.of(userDTO.password()));
    //        userRepository.save(user);
    //    }


}
