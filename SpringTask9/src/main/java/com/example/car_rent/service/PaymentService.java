package com.example.car_rent.service;

public interface PaymentService {
     void handleWebhook(String payload, String signature);
     String createCheckoutSession(String rentalId);
    }
