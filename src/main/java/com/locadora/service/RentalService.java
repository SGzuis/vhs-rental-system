package com.locadora.service;

import com.locadora.domain.entity.*;
import com.locadora.domain.enums.*;
import com.locadora.exception.BusinessException;
import com.locadora.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalService {
    private final RentalRepository rentalRepository;
    private final TapeRepository tapeRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final FineService fineService;
    private final DamageRecordRepository damageRecordRepository;
    
    @Transactional
    public List<Rental> createRentals(String customerId, List<String> tapeIds, Integer days) {
        if (customerService.isBlocked(customerId)) {
            throw new BusinessException("Customer is blocked due to overdue rentals or pending fines");
        }

        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new BusinessException("Customer not found"));
        
        List<Rental> rentals = new java.util.ArrayList<>();
        
        for (String tapeId : tapeIds) {
            Tape tape = tapeRepository.findById(tapeId)
                .orElseThrow(() -> new BusinessException("Tape not found: " + tapeId));
            
            if (tape.getStatus() != TapeStatus.AVAILABLE) {
                throw new BusinessException("Tape is not available for rental: " + tapeId);
            }
            
            LocalDate rentalDate = LocalDate.now();
            LocalDate expectedReturnDate = rentalDate.plusDays(days);
            BigDecimal totalAmount = tape.getMovie().getDailyRate().multiply(BigDecimal.valueOf(days));
            
            Rental rental = Rental.builder()
                .customer(customer)
                .tape(tape)
                .rentalDate(rentalDate)
                .expectedReturnDate(expectedReturnDate)
                .dailyRate(tape.getMovie().getDailyRate())
                .totalAmount(totalAmount)
                .status(RentalStatus.ACTIVE)
                .daysRented(days)
                .build();
            
            tape.setStatus(TapeStatus.RENTED);
            tapeRepository.save(tape);
            rentals.add(rentalRepository.save(rental));
        }
        
        return rentals;
    }

    public List<Rental> findActiveRentalsByCustomer(String customerId) {
        return rentalRepository.findByCustomerIdAndStatus(customerId, RentalStatus.ACTIVE);
    }

    @Transactional
    public Rental renewRental(String rentalId, Integer extraDays) {
        Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new BusinessException("Rental not found"));
        
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new BusinessException("Only active rentals can be renewed");
        }
        
        LocalDate newExpectedDate = rental.getExpectedReturnDate().plusDays(extraDays);
        rental.setExpectedReturnDate(newExpectedDate);
        
        // Atualiza valor total
        BigDecimal additionalAmount = rental.getDailyRate().multiply(BigDecimal.valueOf(extraDays));
        rental.setTotalAmount(rental.getTotalAmount().add(additionalAmount));
        rental.setDaysRented(rental.getDaysRented() + extraDays);
        
        return rentalRepository.save(rental);
    }
    
    @Transactional
    public Rental returnRental(String rentalId, boolean rewound) {
        Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new BusinessException("Rental not found"));
        
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new BusinessException("Rental is not active");
        }
        
        LocalDate actualReturnDate = LocalDate.now();
        rental.setActualReturnDate(actualReturnDate);
        rental.setRewound(rewound);
        
        BigDecimal fineAmount = BigDecimal.ZERO;
        
        // Late fine
        if (actualReturnDate.isAfter(rental.getExpectedReturnDate())) {
            long daysLate = ChronoUnit.DAYS.between(rental.getExpectedReturnDate(), actualReturnDate);
            rental.setDaysLate((int) daysLate);
            fineAmount = fineAmount.add(fineService.calculateLateFine(rental));
            rental.setStatus(RentalStatus.OVERDUE);
        } else {
            rental.setStatus(RentalStatus.RETURNED);
        }

        // Not rewound penalty (fixed value for example)
        if (!rewound) {
            fineAmount = fineAmount.add(BigDecimal.valueOf(2.00));
        }

        rental.setFineAmount(fineAmount);
        
        Tape tape = rental.getTape();
        tape.setStatus(TapeStatus.AVAILABLE);
        tapeRepository.save(tape);
        
        return rentalRepository.save(rental);
    }
    
    @Transactional
    public DamageRecord reportDamage(String rentalId, DamageType damageType, String description) {
        Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new BusinessException("Rental not found"));
        
        BigDecimal damageCost = calculateDamageCost(rental.getTape().getMovie(), damageType);
        
        DamageRecord damage = DamageRecord.builder()
            .rental(rental)
            .damageType(damageType)
            .damageCost(damageCost)
            .description(description)
            .paid(false)
            .build();
        
        if (damageType == DamageType.COMPLETE) {
            rental.getTape().setStatus(TapeStatus.LOST);
        } else {
            rental.getTape().setStatus(TapeStatus.DAMAGED);
        }
        tapeRepository.save(rental.getTape());
        
        return damageRecordRepository.save(damage);
    }
    
    private BigDecimal calculateDamageCost(Movie movie, DamageType damageType) {
        return switch (damageType) {
            case PARTIAL -> movie.getDailyRate().multiply(BigDecimal.valueOf(3));
            case COMPLETE -> movie.getDailyRate().multiply(BigDecimal.valueOf(10));
            default -> BigDecimal.ZERO;
        };
    }
}
