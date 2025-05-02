package com.medvedev.backend.repository;

import com.medvedev.backend.entity.Category;
import com.medvedev.backend.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByType(CategoryType type);

    @Query("SELECT c FROM Category c WHERE c.type = :type")
    List<Category> findByTypeExplicit(@Param("type") CategoryType type); // Optional for more control
}
