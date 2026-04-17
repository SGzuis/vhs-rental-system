package com.locadora.service;

import com.locadora.domain.entity.Fine;
import com.locadora.domain.entity.Rental;
import com.locadora.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {
    private final FineRepository fineRepository;

    public Fine create(Fine fine) {
        fine.setActive(true);
        return fineRepository.save(fine);
    }

    public List<Fine> findAll() {
        return fineRepository.findAll();
    }

    public List<Fine> findActive() {
        return fineRepository.findByActiveTrue();
    }
    
    public BigDecimal calculateLateFine(Rental rental) {
        List<Fine> activeFines = fineRepository.findByActiveTrue();
        
        if (activeFines.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        // Use the first active fine as default
        Fine fine = activeFines.get(0);
        BigDecimal dailyRate = rental.getDailyRate();
        int daysLate = rental.getDaysLate();
        
        return switch (fine.getFineType()) {
            case FIXED_AMOUNT -> fine.getValue();
            case DAILY_PERCENTAGE -> dailyRate
                .multiply(fine.getValue())
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysLate));
            case FIXED_PERCENTAGE -> rental.getTotalAmount()
                .multiply(fine.getValue())
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        };
    }
}
