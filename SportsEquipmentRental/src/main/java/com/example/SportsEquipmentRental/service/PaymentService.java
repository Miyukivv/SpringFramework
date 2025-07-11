package com.example.SportsEquipmentRental.service;

public interface PaymentService {
    void handleWebhook(String payload, String signature);
    String createCheckoutSession(String rentalId);
}
