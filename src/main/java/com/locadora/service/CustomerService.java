package com.locadora.service;

import com.locadora.domain.entity.Customer;
import com.locadora.domain.entity.Rental;
import com.locadora.domain.enums.RentalStatus;
import com.locadora.exception.ResourceNotFoundException;
import com.locadora.repository.CustomerRepository;
import com.locadora.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;

    @Transactional
    public Customer create(Customer customer) {
        customer.setActive(true);
        return customerRepository.save(customer);
    }

    public boolean isBlocked(String id) {
        // Check if has overdue rentals
        List<Rental> overdue = rentalRepository.findByCustomerIdAndStatus(id, RentalStatus.OVERDUE);
        if (!overdue.isEmpty()) return true;
        
        // Check active rentals that are late but status not updated yet
        List<Rental> active = rentalRepository.findByCustomerIdAndStatus(id, RentalStatus.ACTIVE);
        for (Rental r : active) {
            if (r.getExpectedReturnDate().isBefore(LocalDate.now())) return true;
        }
        
        return false;
    }

    public Customer findById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
