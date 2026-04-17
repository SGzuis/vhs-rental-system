package com.locadora.controller;

import com.locadora.domain.entity.Tape;
import com.locadora.service.TapeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tapes")
@RequiredArgsConstructor
public class TapeController {

    private final TapeService tapeService;

    @PostMapping
    public ResponseEntity<Tape> createTape(@RequestParam String movieId, @RequestParam String tapeId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tapeService.createTape(movieId, tapeId));
    }

    @DeleteMapping("/{tapeId}")
    public ResponseEntity<Void> deleteTape(@PathVariable String tapeId) {
        tapeService.deleteTape(tapeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movie/{movieId}/available")
    public ResponseEntity<List<Tape>> getAvailableTapes(@PathVariable String movieId) {
        return ResponseEntity.ok(tapeService.findAvailableTapesByMovie(movieId));
    }
}
