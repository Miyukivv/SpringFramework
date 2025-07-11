package com.example.SportsEquipmentRental.service;

import com.example.SportsEquipmentRental.dto.UserRequest;
import com.example.SportsEquipmentRental.model.User;
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
