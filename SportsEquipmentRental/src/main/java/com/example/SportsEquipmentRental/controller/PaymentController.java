package com.example.SportsEquipmentRental.controller;

import com.example.SportsEquipmentRental.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/checkout/{rentalId}")
    public ResponseEntity<String> createCheckoutSession(@PathVariable String rentalId){
        String url = paymentService.createCheckoutSession(rentalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature){
        System.out.println("Called webhook");
        paymentService.handleWebhook(payload,signature);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/success")
    public ResponseEntity<String> success(){
        System.out.println("Payment accepted");
        return ResponseEntity.ok("success");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel(){
        System.out.println("Payment cancelled");
        return ResponseEntity.ok("cancel");
    }
}
