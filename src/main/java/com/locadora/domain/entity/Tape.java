package com.locadora.domain.entity;

import com.locadora.domain.enums.TapeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tapes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Tape {
    @Id
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TapeStatus status;
    
    @CreatedDate
    private LocalDateTime createdAt;
}
