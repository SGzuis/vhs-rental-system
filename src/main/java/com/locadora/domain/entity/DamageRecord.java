package com.locadora.domain.entity;

import com.locadora.domain.enums.DamageType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "damage_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DamageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;
    
    @Enumerated(EnumType.STRING)
    private DamageType damageType;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal damageCost;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private boolean paid;
    
    @CreatedDate
    private LocalDateTime reportedAt;
}
