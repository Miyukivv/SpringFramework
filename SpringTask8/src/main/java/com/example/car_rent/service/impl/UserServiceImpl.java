package com.example.car_rent.service.impl;

import com.example.car_rent.dto.UserRequest;
import com.example.car_rent.model.User;
import com.example.car_rent.repository.RoleRepository;
import com.example.car_rent.repository.UserRepository;
import com.example.car_rent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.car_rent.model.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void register(UserRequest req) {
        if (userRepository.findByLogin(req.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Error...");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("There is no role... ROLE_USER"));

        User u = User.builder()
                .id(UUID.randomUUID().toString())
                .login(req.getLogin())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(Set.of(userRole))
                .build();
        userRepository.save(u);
    }
    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void softDeleteUser(String userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setActive(false);
            userRepository.save(user);
        });
    }

    @Override
    public List<User> findAllActiveUsers() {
        return userRepository.findAllByIsActiveTrue();
    }
    @Override
    @Transactional
    public void grantRole(String userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.getRoles().add(role);
    }

    @Override
    @Transactional
    public void revokeRole(String userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.getRoles().remove(role);
    }
}
