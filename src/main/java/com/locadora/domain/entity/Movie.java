package com.locadora.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String title;
    
    private String imdbId;
    private String director;
    private String genre;
    private Integer releaseYear;
    
    @Column(columnDefinition = "TEXT")
    private String synopsis;
    
    private String posterUrl;
    private Double imdbRating;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyRate;

    private boolean active;

    @CreatedDate
    private LocalDateTime createdAt;
    }
