package com.example.SportsEquipmentRental.repository;

import com.example.SportsEquipmentRental.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Optional<Role> findByName(String name);
}
