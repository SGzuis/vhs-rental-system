package com.locadora.controller;

import com.locadora.domain.entity.Fine;
import com.locadora.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @PostMapping
    public ResponseEntity<Fine> create(@RequestBody Fine fine) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fineService.create(fine));
    }

    @GetMapping
    public ResponseEntity<List<Fine>> findAll() {
        return ResponseEntity.ok(fineService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Fine>> findActive() {
        return ResponseEntity.ok(fineService.findActive());
    }
}
