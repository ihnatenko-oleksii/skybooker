package com.flightbooking.controller;

import com.flightbooking.dto.PaymentRequest;
import com.flightbooking.dto.PaymentResponse;
import com.flightbooking.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/mock")
    public ResponseEntity<PaymentResponse> processMockPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processMockPayment(request);
        return ResponseEntity.ok(response);
    }


    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
