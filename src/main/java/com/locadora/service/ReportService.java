package com.locadora.service;

import com.locadora.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final RentalRepository rentalRepository;
    
    @Value
    public static class DailyReport {
        LocalDate date;
        long totalRentals;
        long totalReturns;
        BigDecimal totalRevenue;
        BigDecimal totalFinesCollected;
    }
    
    public DailyReport getDailyReport(LocalDate date) {
        long totalRentals = rentalRepository.countByRentalDate(date);
        long totalReturns = rentalRepository.countByActualReturnDate(date);
        
        // Simulação de cálculo de receita e multas (seria necessário somar os campos no repository)
        BigDecimal totalRevenue = BigDecimal.ZERO; 
        BigDecimal totalFinesCollected = BigDecimal.ZERO;
        
        return new DailyReport(date, totalRentals, totalReturns, 
                              totalRevenue, totalFinesCollected);
    }
}
