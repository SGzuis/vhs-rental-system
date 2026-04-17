package com.locadora.service;

import com.locadora.domain.entity.Movie;
import com.locadora.domain.entity.Tape;
import com.locadora.domain.enums.TapeStatus;
import com.locadora.exception.BusinessException;
import com.locadora.repository.MovieRepository;
import com.locadora.repository.TapeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TapeService {

    private final TapeRepository tapeRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public Tape createTape(String movieId, String tapeId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new BusinessException("Movie not found: " + movieId));

        if (tapeRepository.existsById(tapeId)) {
            throw new BusinessException("Tape ID already exists: " + tapeId);
        }

        Tape tape = Tape.builder()
                .id(tapeId)
                .movie(movie)
                .status(TapeStatus.AVAILABLE)
                .build();

        return tapeRepository.save(tape);
    }

    @Transactional
    public void deleteTape(String tapeId) {
        Tape tape = tapeRepository.findById(tapeId)
                .orElseThrow(() -> new BusinessException("Tape not found: " + tapeId));

        if (tape.getStatus() != TapeStatus.AVAILABLE) {
            throw new BusinessException("Cannot delete tape that is rented, damaged, or lost.");
        }

        tapeRepository.delete(tape);
    }

    public List<Tape> findAvailableTapesByMovie(String movieId) {
        return tapeRepository.findByMovieIdAndStatus(movieId, TapeStatus.AVAILABLE);
    }
}
