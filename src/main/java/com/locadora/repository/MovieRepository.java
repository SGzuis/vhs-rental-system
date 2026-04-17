package com.locadora.repository;

import com.locadora.domain.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    Optional<Movie> findByImdbId(String imdbId);
    List<Movie> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT DISTINCT m FROM Movie m JOIN Tape t ON t.movie = m WHERE t.status = 'AVAILABLE' AND m.active = true")
    List<Movie> findAvailableMovies();
}
