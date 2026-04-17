package com.locadora.controller;

import com.locadora.domain.entity.Rental;
import com.locadora.service.RentalService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;
    
    @PostMapping
    public ResponseEntity<List<Rental>> createRental(
            @RequestParam @NotBlank String customerId,
            @RequestParam List<String> tapeIds,
            @RequestParam @Min(1) Integer days) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(rentalService.createRentals(customerId, tapeIds, days));
    }
    
    @PutMapping("/{rentalId}/return")
    public ResponseEntity<Rental> returnRental(
            @PathVariable String rentalId,
            @RequestParam(defaultValue = "true") boolean rewound) {
        return ResponseEntity.ok(rentalService.returnRental(rentalId, rewound));
    }

    @GetMapping("/customer/{customerId}/active")
    public ResponseEntity<List<Rental>> getActiveRentals(@PathVariable String customerId) {
        return ResponseEntity.ok(rentalService.findActiveRentalsByCustomer(customerId));
    }

    @PutMapping("/{rentalId}/renew")
    public ResponseEntity<Rental> renewRental(
            @PathVariable String rentalId,
            @RequestParam @Min(1) Integer extraDays) {
        return ResponseEntity.ok(rentalService.renewRental(rentalId, extraDays));
    }
}
