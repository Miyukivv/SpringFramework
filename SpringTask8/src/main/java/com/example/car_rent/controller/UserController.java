package com.example.car_rent.controller;

import com.example.car_rent.model.User;
import com.example.car_rent.repository.UserRepository;
import com.example.car_rent.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/users")
@RestController
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllActiveUsers();
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> softDeleteUser(@PathVariable String userId) {
        userService.softDeleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<Void> addRoleToUser(@PathVariable String userId, @RequestBody String roleName) {
        userService.grantRole(userId, roleName);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable String userId,@PathVariable String roleName) {
        userService.revokeRole(userId, roleName);
        return ResponseEntity.ok().build();
    }
}
