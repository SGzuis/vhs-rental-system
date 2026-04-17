package com.locadora.controller;

import com.locadora.repository.RentalRepository;
import com.locadora.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final RentalRepository rentalRepository;
    private final ReportService reportService;
    
    @GetMapping("/most-rented")
    public ResponseEntity<List<Object[]>> getMostRentedMovies(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(rentalRepository.findMostRentedMovies(limit));
    }

    @GetMapping("/daily-report")
    public ResponseEntity<ReportService.DailyReport> getDailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.getDailyReport(date));
    }
}
