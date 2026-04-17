package com.locadora.repository;

import com.locadora.domain.entity.Rental;
import com.locadora.domain.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    long countByRentalDate(LocalDate date);
    
    long countByActualReturnDate(LocalDate date);

    @Query(value = "SELECT m.title, COUNT(r.id) as rental_count " +
           "FROM rentals r JOIN tapes t ON r.tape_id = t.id JOIN movies m ON t.movie_id = m.id " +
           "GROUP BY m.id, m.title ORDER BY rental_count DESC LIMIT :limit", 
           nativeQuery = true)
    List<Object[]> findMostRentedMovies(@Param("limit") int limit);
}
