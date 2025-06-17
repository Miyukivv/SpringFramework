package com.example.car_rent.controller;

import com.example.car_rent.model.User;
import com.example.car_rent.model.Vehicle;
import com.example.car_rent.repository.UserRepository;
import com.example.car_rent.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final UserRepository userRepository;
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService, UserRepository userRepository) {
        this.vehicleService = vehicleService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Vehicle> getAll(){
        return vehicleService.findAll();
    }
    @GetMapping("/available")
    public List<Vehicle> getAvailableVehicles(){
        return vehicleService.findAvailableVehicles();
    }

    @GetMapping("/active")
    public List<Vehicle> getActive(){
        return vehicleService.findAllActive();
    }

    @GetMapping("/rented")
    public List<Vehicle> getRented(){
        return  vehicleService.findRentedVehicles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable String id) {
        return vehicleService.findById(id)
                .map(vehicle -> {
                    return ResponseEntity.ok(vehicle);
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();//404
                });
    }

    @PostMapping("/admin/addVehicle")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle, @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user =userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Uzytkownik nie znaleziony " + login));

        try {
            Vehicle savedVehicle = vehicleService.save(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/admin/deleteVehicle/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> softDelete(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        String login = userDetails.getUsername();
        User user =userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Uzytkownik nie znaleziony " + login));

        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
