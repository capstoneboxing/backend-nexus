package com.nexus.repository;

import com.nexus.model.AllTimeRankedBoxer;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllTimeRankedBoxerRepository extends ListCrudRepository<AllTimeRankedBoxer, Integer> {
    List<AllTimeRankedBoxer> findByBatchIdOrderByRankingPositionAsc(Integer batchId);
}