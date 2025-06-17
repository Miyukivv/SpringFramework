package com.example.car_rent.controller;

import com.example.car_rent.dto.RentalRequest;
import com.example.car_rent.model.Rental;
import com.example.car_rent.model.User;
import com.example.car_rent.repository.UserRepository;
import com.example.car_rent.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserRepository userRepository;

    //Potrzebowalismy dodac drugi argument, aby miec dostep do informacji o zalogowanym uzytkowniku
    @PostMapping("/rent")
    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user =userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Uzytkownik nie znaleziony " + login));
        try {
            Rental rental = rentalService.rent(rentalRequest.getVehicleId(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/return")
    public ResponseEntity<Rental> returnBack(@RequestBody RentalRequest req,  @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user =userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Uzytkownik nie znaleziony " + login));

        boolean ok = rentalService.returnRental(req.getVehicleId(), user.getId());
        return ok
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/allRentals")
    public List<Rental> allRentals() {
        return rentalService.findAll();
    }
}
