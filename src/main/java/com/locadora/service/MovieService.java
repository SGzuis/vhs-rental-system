package com.locadora.service;

import com.locadora.client.OmdbClient;
import com.locadora.client.OmdbResponse;
import com.locadora.domain.entity.Movie;
import com.locadora.exception.ResourceNotFoundException;
import com.locadora.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {
    private final MovieRepository movieRepository;
    private final OmdbClient omdbClient;
    
    @Value("${omdb.api.key}")
    private String omdbApiKey;
    
    @Transactional
    public Movie createMovie(Movie movie) {
        validateMovie(movie);
        movie.setActive(true);
        return movieRepository.save(movie);
    }
    
    @Cacheable(value = "movies", key = "#id")
    public Movie findById(String id) {
        return movieRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }
    
    @Transactional
    @CacheEvict(value = "movies", key = "#id")
    public Movie updateMovie(String id, Movie movieDetails) {
        Movie movie = findById(id);
        movie.setTitle(movieDetails.getTitle());
        movie.setDailyRate(movieDetails.getDailyRate());
        return movieRepository.save(movie);
    }
    
    @Transactional
    public Movie importFromOmdb(String imdbId) {
        OmdbResponse response = omdbClient.getMovieByImdbId(imdbId, omdbApiKey);
        
        if (!"True".equals(response.Response())) {
            throw new RuntimeException("Movie not found in OMDb: " + response.Error());
        }
        
        Movie movie = Movie.builder()
            .title(response.Title())
            .imdbId(response.imdbID())
            .director(response.Director())
            .genre(response.Genre())
            .releaseYear(parseYear(response.Year()))
            .synopsis(response.Plot())
            .posterUrl(response.Poster())
            .imdbRating(parseRating(response.imdbRating()))
            .dailyRate(BigDecimal.valueOf(5.0))
            .active(true)
            .build();
        
        return movieRepository.save(movie);
    }
    
    private void validateMovie(Movie movie) {
        if (movie.getDailyRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Daily rate must be positive");
        }
    }

    private Integer parseYear(String year) {
        try {
            return Integer.parseInt(year.substring(0, 4));
        } catch (Exception e) {
            return null;
        }
    }

    private Double parseRating(String rating) {
        try {
            return Double.parseDouble(rating);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
