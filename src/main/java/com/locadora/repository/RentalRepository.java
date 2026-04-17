package com.locadora.repository;

import com.locadora.domain.entity.Rental;
import com.locadora.domain.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, String> {
    List<Rental> findByCustomerIdAndStatus(String customerId, RentalStatus status);
    List<Rental> findByStatusAndExpectedReturnDateBefore(RentalStatus status, LocalDate date);
    List<Rental> findByCustomerId(String customerId);
    
    @Query("SELECT r FROM Rental r WHERE r.status = 'ACTIVE' AND r.expectedReturnDate < CURRENT_DATE")
    List<Rental> findOverdueRentals();
}
