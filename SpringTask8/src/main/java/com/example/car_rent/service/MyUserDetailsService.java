package com.example.car_rent.service;

import com.example.car_rent.model.User;
import com.example.car_rent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Wyszukujemy uzytkownika po polu 'login'
        User user = userRepository.findByLoginAndIsActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found or inactive"));
        //Zbudujemy obiekt UserDetails na podstawie danych encji User
        //Tworzymy liste Grantow (autoryzacji) z roli uzytkownika:
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList();

        return new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPassword(),
            authorities
            );
        }

    }
