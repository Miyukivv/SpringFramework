package com.example.SportsEquipmentRental.controller;

import com.example.SportsEquipmentRental.dto.RentalRequest;
import com.example.SportsEquipmentRental.model.Rental;
import com.example.SportsEquipmentRental.model.User;
import com.example.SportsEquipmentRental.repository.UserRepository;
import com.example.SportsEquipmentRental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final UserRepository userRepository;

    @PostMapping("/rent")
    public ResponseEntity<Rental> rentEquipment(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user =userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Uzytkownik nie znaleziony " + login));
        try {
            Rental rental = rentalService.rent(rentalRequest.getEquipmentId(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/return")
    public ResponseEntity<Rental> returnBack(@RequestBody RentalRequest req,  @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user =userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Uzytkownik nie znaleziony " + login));

        boolean ok = rentalService.returnRental(req.getEquipmentId(), user.getId());
        return ok
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/allRentals")
    public List<Rental> allRentals() {
        return rentalService.findAll();
    }

}
