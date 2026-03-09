package customer_service.user.configuration;

import customer_service.customer.application.UserDetailsServiceCustomer;
import customer_service.user.appliaction.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(@Lazy CustomUserDetailsService customUserDetailsService) {
       this.customUserDetailsService = customUserDetailsService;
    }


    //TODO Admin darf user anlegen, kunden darf nur sich selber sheen mit customer/me , außerdem PATCH nur user ihrer eigene Daten pahcen dürfen customer/me/homeAddress
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

                        // ADMIN ENDPOINTS
                        .requestMatchers("POST", "/customers").hasAnyRole("USER", "ADMIN")

                        // User darf nur „me“ ändern
                        .requestMatchers("PATCH", "/customers/me/mailAddress").hasRole("USER")
                        .requestMatchers("PATCH", "/customers/me/homeAddress").hasRole("USER")


                        // NUR ADMIN ENDPOINTS
                        .requestMatchers("DELETE", "/customers/*").hasRole("ADMIN")
                        .requestMatchers("PATCH", "/customers/*/mailAddress").hasAnyRole("ADMIN","USER")
                        .requestMatchers("PATCH", "/customers/*/homeAddress").hasAnyRole("ADMIN","USER")

                        // Alle anderen Requests müssen authentifiziert sein
                        .anyRequest().authenticated()
                )

                // Basic Authentication aktivieren
                .httpBasic(httpBasic -> {
                })

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