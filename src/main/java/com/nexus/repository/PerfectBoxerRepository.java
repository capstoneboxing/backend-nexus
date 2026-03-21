package com.nexus.repository;

import com.nexus.model.PerfectBoxer;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfectBoxerRepository extends ListCrudRepository<PerfectBoxer, Integer> {
    Optional<PerfectBoxer> findByWeightClassId(Integer weightClassId);
    Optional<PerfectBoxer> findByBatchId(Integer batchId);
}