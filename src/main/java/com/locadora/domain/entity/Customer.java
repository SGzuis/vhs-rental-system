package com.locadora.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String document;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String phone;
    private String address;
    
    private boolean active;
    
    @CreatedDate
    private LocalDateTime registeredAt;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Rental> rentals = new HashSet<>();
}
