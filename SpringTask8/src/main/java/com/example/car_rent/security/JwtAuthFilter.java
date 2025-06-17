package com.example.car_rent.security;

import com.example.car_rent.model.User;
import com.example.car_rent.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;

//Filtr zostanie wykonany tylko raz dla kazdego przychodzacego żądania
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil; //do zaimplementowania - operacje na tokenach JWT

    //Serwis do ładowania danych użytkownika
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    //Główna metoda filtra, która jest wywoływana dla każdego przychodzącego żądania HTTP
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //token JwT jest przesylany w tym naglowku
        String authHeader = request.getHeader("Authorization");

        //weryfikacja tokena na podstawie danych uzytkownika
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //Brak naglowka Authorization lub niepoprawny format -> przekazujemy żądanie dalej (nie autentykujemy)
            filterChain.doFilter(request, response);
            return;
        }
        //Gdy istnieje naglowek w formacie "Bearer <token>"
        String token = authHeader.substring(7); //sam token
        try {
            String username = jwtUtil.extractUsername(token);

            //Sprawdzamy, czy uzytkownik nie jest juz uwierzytelniony, np. przez inne mechanizmy
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //szczegoly uzytkownika z bazy danych
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                User user = userRepository.findByLoginAndIsActiveTrue(username).orElse(null);
                if (user == null) {
                    // user jest nieaktywny lub nie istnieje -> nie autoryzuj
                    filterChain.doFilter(request, response);
                    return;
                }


                //weryfikacja tokena na podstawie danych uzytkownika
                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //Dodatkowe informacje z requesta -ip klienta(id sesji nie ma)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //Ustawiamy kontekst bezpieczeństwa - użytkownik zalogowany
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JwtException e){
            //brak Authentication w SecurityContext
        }
        //żądanie idzie dalej w łańcuchu filtrów
        filterChain.doFilter(request,response);
    }
}
