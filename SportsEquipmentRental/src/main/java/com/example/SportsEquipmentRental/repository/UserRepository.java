package com.example.SportsEquipmentRental.repository;

import com.example.SportsEquipmentRental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByLogin(String login);
    List<User> findAll();
    Optional<User> findById(String id);
    User save(User user);

    void deleteById(String id);
    Optional<User> findByLoginAndIsActiveTrue(String login);
    List<User> findAllByIsActiveTrue();
}
