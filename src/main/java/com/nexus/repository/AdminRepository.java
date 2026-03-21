package com.nexus.repository;

import com.nexus.model.Admin;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends ListCrudRepository<Admin, Integer> {

    @Query("SELECT * FROM admin WHERE username = :username")
    Optional<Admin> findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}