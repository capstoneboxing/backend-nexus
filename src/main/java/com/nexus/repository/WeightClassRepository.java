package com.nexus.repository;

import com.nexus.model.WeightClass;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WeightClassRepository extends ListCrudRepository<WeightClass, Integer> {

    @Query("SELECT * FROM weight_class WHERE class_name = :class_name")
    Optional<WeightClass> findByClassName(@Param("class_name") String className);
}
