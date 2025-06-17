package com.example.car_rent.security;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //adnotacja konfiguracji - definicja beanów
@EnableWebSecurity //włączamy mechanizm WebSecurity!
@EnableMethodSecurity //włączamy zabezpieczania metod - np. @PreAuthorize
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            //Obiekt do łańcucha ustawień obsługi bezpieczeństwa:
            HttpSecurity http,

            //Niestandardowy filtr do implementacji - weryfikacja
            JwtAuthFilter jwtAuthFilter,

            //Dostawca uwierzytelnienia tokenu
            AuthenticationProvider authProvider
    ) throws Exception {
        //w rest api - brak sesji i ciasteczek, wyłączamy ochronę //Cross-Site Request Forgery
        http.csrf(csrf -> csrf.disable())
                //konfigurator autoryzacji i ustalanie reguł:
                .authorizeHttpRequests(auth -> auth
                        //endpointy do auth(login, register)
                        .requestMatchers("/api/auth/**").permitAll()

                        //endpointy dla role admin (zmienic w bazie usera na role_user i role_admin)
                        .requestMatchers("/api/admin/**").hasRole("admin")
                        .anyRequest().authenticated()
                )
                //Brak sesji, rest api z tokenami jwt!
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Dostawca uwierzytelniania
                .authenticationProvider(authProvider)
                //Weryfikacja tokenu przed standardowym sposobem autoryzacji!
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean //Bean z dostawcą autentykacji
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); //Obiekt do Autentykacji z bazy
        provider.setUserDetailsService(userDetailsService); //serwis uzywany do autentykacji
        provider.setPasswordEncoder(passwordEncoder); //encoder
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        //Konfiguracja uwierzytelniania
            AuthenticationConfiguration config) throws Exception {
        //Dostep do AuthenticationManagera
        return config.getAuthenticationManager();
    }

}

