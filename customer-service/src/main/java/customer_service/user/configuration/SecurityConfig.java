package customer_service.user.configuration;

import customer_service.user.appliaction.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF deaktivieren für REST API
                .csrf(csrf -> csrf.disable())

                // Authorization Rules
                .authorizeHttpRequests(auth -> auth
                        // ÖFFENTLICHE ENDPOINTS
                        .requestMatchers("GET", "/customers").permitAll()
                        .requestMatchers("GET", "/customers/*").permitAll()

                        // USER + ADMIN ENDPOINTS
                        .requestMatchers("POST", "/customers").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("PATCH", "/customers/*/mailAddress").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("PATCH", "/customers/*/homeAddress").hasAnyRole("USER", "ADMIN")

                        // NUR ADMIN ENDPOINTS
                        .requestMatchers("DELETE", "/customers/*").hasRole("ADMIN")

                        // Alle anderen Requests müssen authentifiziert sein
                        .anyRequest().authenticated()
                )

                // Basic Authentication aktivieren
                .httpBasic(httpBasic -> {})

                // Stateless Session Management
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}