package com.example.ecommerce.security;

import com.example.ecommerce.enums.Roles;
import com.example.ecommerce.model.Role;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.security.jwt.AuthEntryPoint;
import com.example.ecommerce.security.jwt.AuthTokenFilter;
import com.example.ecommerce.security.services.UserDetailsSvcImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsSvcImpl userDetailsSvc;
    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Bean
    public AuthTokenFilter authenticationJwtFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsSvc);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationConfiguration(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
                        requests.requestMatchers("/api/auth/**",
                                        "/api/public/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/swagger-resources/configuration/ui",
                                        "/swagger-resources/configuration/security",
                                        "/webjars/**").permitAll()
                                .requestMatchers("/api/admin/**").hasAuthority(Roles.ROLE_ADMIN.name())
                                .anyRequest().authenticated()
                ) // authenticate all other endpoints
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define stateless sessions (no session id, rather tokens)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint)) // In case of error while authenticating, errors will be handles by this class
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // needed for h2 console frames
                .addFilterBefore(authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class) // placement of custom JWT authenticating filter.
                .authenticationProvider(authenticationProvider());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v3/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/error"));
    }


    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository) {
        return args -> {
            // Retrieve or create roles
            Object AppRole;
            Role userRole = roleRepository.findByRoleName(Roles.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(Roles.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRoleName(Roles.ROLE_MERCHANT)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(Roles.ROLE_MERCHANT);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(Roles.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(Roles.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });
        };
    }
}
