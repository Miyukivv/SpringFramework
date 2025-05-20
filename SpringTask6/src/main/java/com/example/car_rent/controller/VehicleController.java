package com.example.car_rent.controller;

import com.example.car_rent.model.Vehicle;
import com.example.car_rent.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
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

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle savedVehicle = vehicleService.save(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<Void> softDelete(@PathVariable String id){
        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
