package com.example.car_rent.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;  //Secret key - do podpisywania tokenu
    @Value("${jwt.expiration}")
    private long expirationMs; //czas waznosci tokenu

//Metoda do generowania tokenu:
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", getUserRole(userDetails)); //dodanie niestandardowego Claim (oswiadczenia) dla  roli Usera

        Date now = new Date();
        //data wygasniecia tokenu:
        Date expirationDate = new Date(now.getTime() + expirationMs);
        //budowanie tokenu
        return Jwts.builder()
                .header().type("JWT").and() //opcjonalny typ tokenu
                .claims().add(claims)               //niestandardowe Claimsy - dodatkowa informacja o userze
                .and()                              //dalsze budowanie
                .subject(userDetails.getUsername()) //podmiot tokenu - u nas username/login
                .issuedAt(now)                      //data wystawienia
                .expiration(expirationDate)         //data wygasniecia
                .signWith(getSigningKey())          //podpisanie tokenu przy uzyciu klucza
                .compact();                         //token jako ciag znakow
    }

    public String extractUsername(String token){
        return getClaims(token).getSubject(); //pobranie nazwy usera z tokenu
    }

    //Walidacja tokenu - czy username czy podpis sie zgadza, oraz czy token nie wygasl
    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);

        if (!username.equals(userDetails.getUsername())){
            return false;
        }
        return !getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token){
        Jws<Claims> jwsClaims = Jwts.parser()   //parsertokena
                .verifyWith(getSigningKey())    //sprawdzanie podpisu tokena
                .build()                        //budowanie parsera
                .parseSignedClaims(token);      //Gdy prawidlowy parsowanie tokena
        return jwsClaims.getPayload();          //zwrocenie claims(body) - dane usera - cialo tokenu.
    }

    //Zwraca
    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //Dekoduje secretKey z base64url z pliku
        return Keys.hmacShaKeyFor(keyBytes);                 //generuje SecretKey dla algorytmu HMAC-SHA
        //HMAC to metoda szyfrowania, ktora wykorzystuje funkcje skrotu (np. SHA-256) i klucz tajny do generowania podpisu
    }
    //Zwraca role usera
    private String getUserRole(UserDetails userDetails){
        return userDetails.getAuthorities().stream() //Zwraca role usera
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);
    }
}
