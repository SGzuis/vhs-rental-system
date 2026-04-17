package com.locadora.repository;

import com.locadora.domain.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, String> {
    List<Fine> findByActiveTrue();
}
