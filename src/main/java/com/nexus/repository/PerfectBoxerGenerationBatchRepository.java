package com.nexus.repository;

import com.nexus.model.PerfectBoxerGenerationBatch;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfectBoxerGenerationBatchRepository extends ListCrudRepository<PerfectBoxerGenerationBatch, Integer> {

    Optional<PerfectBoxerGenerationBatch> findByWeightClassIdAndIsActiveTrue(Integer weightClassId);

    @Modifying
    @Query("UPDATE perfect_boxer_generation_batch SET is_active = FALSE WHERE weight_class_id = :weightClassId AND is_active = TRUE")
    void deactivateActiveBatchByWeightClassId(Integer weightClassId);
}