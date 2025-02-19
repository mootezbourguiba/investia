package tn.esprit.investia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Désactive CSRF explicitement
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/crypto/**").permitAll() // Autorise toutes les routes /api/crypto/**
                        .anyRequest().authenticated() // Toutes les autres routes nécessitent une authentification
                )
                .httpBasic(Customizer.withDefaults()); // Utilise Basic Authentication par défaut

        return http.build();
    }
}