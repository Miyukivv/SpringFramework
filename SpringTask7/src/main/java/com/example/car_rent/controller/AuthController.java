package com.example.car_rent.controller;

import com.example.car_rent.dto.LoginRequest;
import com.example.car_rent.dto.LoginResponse;
import com.example.car_rent.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        //1. Uwierzytelnienie uzytkownika za pomoca podanego loginu i hasla
        Authentication auth;
        try{
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
            );
            //Jesli po wywolaniu nie rzucono wyjatku, oznacza to, ze dane sa poprawne
        } catch (BadCredentialsException e){
            //Niepoprawne dane logowania
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //2. Uwierzytelnienie sie powiodlo, mozna wygenerowac token JWT
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        //3. Zwracamy token w odpowiedzi
        LoginResponse responseBody = new LoginResponse(token);
        return ResponseEntity.ok(responseBody);
    }
}
