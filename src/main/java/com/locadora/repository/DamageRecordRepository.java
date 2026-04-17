package com.locadora.repository;

import com.locadora.domain.entity.DamageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageRecordRepository extends JpaRepository<DamageRecord, String> {
}
