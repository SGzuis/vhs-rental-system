package com.locadora.controller;

import com.locadora.domain.entity.Movie;
import com.locadora.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    
    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(movie));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable String id) {
        return ResponseEntity.ok(movieService.findById(id));
    }
    
    @PostMapping("/import/{imdbId}")
    public ResponseEntity<Movie> importFromOmdb(@PathVariable String imdbId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.importFromOmdb(imdbId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable String id, @Valid @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.updateMovie(id, movie));
    }
}
