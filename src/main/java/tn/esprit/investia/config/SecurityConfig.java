package tn.esprit.investia.config;

import tn.esprit.investia.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers("/login").permitAll() // Permet d'accéder à la page de login
                                .anyRequest().authenticated() // Nécessite une authentification pour les autres pages
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // Page de login personnalisée
                                .permitAll() // Permet à tous d'accéder à la page de login
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService; // Utilisation du service pour récupérer les détails de l'utilisateur
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utilisation du BCrypt pour encoder les mots de passe
    }
}