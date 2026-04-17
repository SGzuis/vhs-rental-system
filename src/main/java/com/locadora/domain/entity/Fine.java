package com.locadora.domain.entity;

import com.locadora.domain.enums.FineType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private FineType fineType;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;
    
    private boolean active;
    
    @CreatedDate
    private LocalDateTime createdAt;
}
