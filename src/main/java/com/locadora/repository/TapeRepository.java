package com.locadora.repository;

import com.locadora.domain.entity.Tape;
import com.locadora.domain.enums.TapeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TapeRepository extends JpaRepository<Tape, String> {
    List<Tape> findByMovieIdAndStatus(String movieId, TapeStatus status);
}
