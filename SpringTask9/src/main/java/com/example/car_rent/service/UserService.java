package com.example.car_rent.service;

import com.example.car_rent.dto.UserRequest;
import com.example.car_rent.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    void register(UserRequest req);
    Optional<User> findByLogin(String login);
    void softDeleteUser(String userId);
    List<User> findAllActiveUsers();
    void grantRole(String userId, String roleName);
    void revokeRole(String userId, String roleName);
}
