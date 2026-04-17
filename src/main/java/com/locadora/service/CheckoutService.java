package com.locadora.service;

import com.locadora.domain.entity.Rental;
import com.locadora.domain.enums.RentalStatus;
import com.locadora.exception.BusinessException;
import com.locadora.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final RentalRepository rentalRepository;

    public BigDecimal calculatePendingAmount(String customerId) {
        List<Rental> activeRentals = rentalRepository.findByCustomerIdAndStatus(customerId, RentalStatus.ACTIVE);
        List<Rental> overdueRentals = rentalRepository.findByCustomerIdAndStatus(customerId, RentalStatus.OVERDUE);

        BigDecimal total = BigDecimal.ZERO;

        for (Rental rental : activeRentals) {
            // Se estiver atrasado mas o status ainda não foi atualizado no banco (processo batch não rodou)
            if (rental.getExpectedReturnDate().isBefore(LocalDate.now())) {
                long daysLate = LocalDate.now().toEpochDay() - rental.getExpectedReturnDate().toEpochDay();
                BigDecimal fine = rental.getDailyRate()
                        .multiply(BigDecimal.valueOf(0.10)) // 10% de multa diária por exemplo
                        .multiply(BigDecimal.valueOf(daysLate));
                total = total.add(fine);
            }
        }

        for (Rental rental : overdueRentals) {
            if (rental.getFineAmount() != null) {
                total = total.add(rental.getFineAmount());
            }
        }

        return total;
    }

    @Transactional
    public void processPayment(String customerId, BigDecimal amountPaid) {
        BigDecimal pending = calculatePendingAmount(customerId);

        if (amountPaid.compareTo(pending) < 0) {
            throw new BusinessException("Insufficient payment amount. Pending: " + pending);
        }

        // Marca locações atrasadas como regularizadas
        List<Rental> overdueRentals = rentalRepository.findByCustomerIdAndStatus(customerId, RentalStatus.OVERDUE);
        for (Rental rental : overdueRentals) {
            rental.setStatus(RentalStatus.RETURNED);
            rental.setFineAmount(BigDecimal.ZERO);
            rentalRepository.save(rental);
        }

        // Para locações ativas que estavam atrasadas, atualiza o status se o pagamento cobre a multa estimada
        List<Rental> activeRentals = rentalRepository.findByCustomerIdAndStatus(customerId, RentalStatus.ACTIVE);
        for (Rental rental : activeRentals) {
            if (rental.getExpectedReturnDate().isBefore(LocalDate.now())) {
                rental.setStatus(RentalStatus.RETURNED);
                rental.setActualReturnDate(LocalDate.now());
                rentalRepository.save(rental);
            }
        }
    }
}
