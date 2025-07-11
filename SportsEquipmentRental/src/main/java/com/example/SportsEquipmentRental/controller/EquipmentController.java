package com.example.SportsEquipmentRental.controller;

import com.example.SportsEquipmentRental.model.*;
import com.example.SportsEquipmentRental.repository.UserRepository;
import com.example.SportsEquipmentRental.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    private final UserRepository userRepository;
    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(UserRepository userRepository, EquipmentService equipmentService) {
        this.userRepository = userRepository;
        this.equipmentService=equipmentService;
    }

    @GetMapping
    public List<Equipment> getAll() {
        return equipmentService.findAll();
    }


    @GetMapping("/available")
    public List<Equipment> getAvailableEquipment(){
        return equipmentService.findAvailableEquipment();
    }

    @GetMapping("/active")
    public List<Equipment> getActive(){
        return equipmentService.findAllActive();
    }
    @GetMapping("/rented")
    public List<Equipment> getRented(){
        return  equipmentService.findRentedEquipment();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getById(@PathVariable String id) {
        return equipmentService.findById(id)
                .map(equipment -> {
                    return ResponseEntity.ok(equipment);
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();//404
                });
    }

    @PostMapping("/admin/addEquipment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Equipment> addEquipment(@RequestBody Equipment equipment, @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user =userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Uzytkownik nie znaleziony " + login));

        try {
            Equipment savedEquipment = equipmentService.save(equipment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/admin/deleteEquipment/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> softDelete(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        String login = userDetails.getUsername();
        User user =userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Uzytkownik nie znaleziony " + login));

        equipmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Equipment>> getByType(@PathVariable TypeOfEquipment type) {
        List<Equipment> list = equipmentService.findByType(type);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/type/{type}/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipmentTypeStats> getStatsByType(@PathVariable TypeOfEquipment type) {
        EquipmentTypeStats stats = equipmentService.statsByType(type);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/age/{ageGroup}")
    public ResponseEntity<List<Equipment>> getByAgeGroup(@PathVariable AgeGroup ageGroup) {
        List<Equipment> list = equipmentService.findByAgeGroup(ageGroup);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}
