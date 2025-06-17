package com.example.car_rent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.car_rent.model.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,String> {
    Optional<Role> findByName(String name);

}
