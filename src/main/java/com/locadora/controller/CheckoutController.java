package com.locadora.controller;

import com.locadora.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/payment/{customerId}")
    public ResponseEntity<Void> processPayment(
            @PathVariable String customerId,
            @RequestParam BigDecimal amountPaid) {
        checkoutService.processPayment(customerId, amountPaid);
        return ResponseEntity.ok().build();
    }
}
