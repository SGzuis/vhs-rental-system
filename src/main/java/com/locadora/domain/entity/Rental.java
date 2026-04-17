package com.locadora.domain.entity;

import com.locadora.domain.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "tape_id", nullable = false)
    private Tape tape;
    
    @Column(nullable = false)
    private LocalDate rentalDate;

    @Column(nullable = false)
    private LocalDate expectedReturnDate;
    
    private LocalDate actualReturnDate;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyRate;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal fineAmount;
    
    @Enumerated(EnumType.STRING)
    private RentalStatus status;
    
    private Integer daysLate;
    private Integer daysRented;
    
    @Builder.Default
    private boolean rewound = true;
    
    @CreatedDate
    private LocalDateTime createdAt;
}
