package com.locadora.controller;

import com.locadora.domain.entity.Customer;
import com.locadora.service.CheckoutService;
import com.locadora.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable String id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<Customer>> getBlockedCustomers() {
        return ResponseEntity.ok(customerService.findBlockedCustomers());
    }

    @GetMapping("/{id}/pending")
    public ResponseEntity<java.math.BigDecimal> getPendingAmount(@PathVariable String id) {
        return ResponseEntity.ok(checkoutService.calculatePendingAmount(id));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }
}
