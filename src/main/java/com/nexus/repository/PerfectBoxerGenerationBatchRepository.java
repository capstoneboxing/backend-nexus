package com.nexus.repository;

import com.nexus.model.PerfectBoxerGenerationBatch;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfectBoxerGenerationBatchRepository extends ListCrudRepository<PerfectBoxerGenerationBatch, Integer> {

    Optional<PerfectBoxerGenerationBatch> findByWeightClassIdAndIsActiveTrue(Integer weightClassId);

    @Modifying
    @Query("UPDATE perfect_boxer_generation_batch SET is_active = FALSE WHERE weight_class_id = :weightClassId AND is_active = TRUE")
    void deactivateActiveBatchByWeightClassId(Integer weightClassId);

    @Modifying
    @Query("""
        UPDATE perfect_boxer_generation_batch
        SET status = :status, error_message = :errorMessage
        WHERE batch_id = :batchId
        """)
    void updateStatus(Integer batchId, String status, String errorMessage);

    @Modifying
    @Query("UPDATE perfect_boxer_generation_batch SET is_active = FALSE WHERE batch_id = :batchId")
    void deactivateBatch(Integer batchId);

    @Modifying
    @Query("""
    UPDATE perfect_boxer_generation_batch
    SET status = :status,
        error_message = :errorMessage,
        is_active = :isActive
    WHERE batch_id = :batchId
""")
    void updateStatusAndIsActive(
            @Param("batchId") Integer batchId,
            @Param("status") String status,
            @Param("isActive") Boolean isActive,
            @Param("errorMessage") String errorMessage
    );

    @Modifying
    @Query("""
    UPDATE perfect_boxer_generation_batch
    SET is_active = FALSE
    WHERE weight_class_id = :weightClassId
      AND batch_id <> :batchId
      AND is_active = TRUE
""")
    void deactivateOtherActiveBatchesByWeightClassId(Integer weightClassId, Integer batchId);

    List<PerfectBoxerGenerationBatch> findByIsActiveTrue();
}