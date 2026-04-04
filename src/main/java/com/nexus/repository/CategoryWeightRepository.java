package com.nexus.repository;

import com.nexus.model.CategoryWeight;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryWeightRepository extends ListCrudRepository<CategoryWeight, Integer> {


}
