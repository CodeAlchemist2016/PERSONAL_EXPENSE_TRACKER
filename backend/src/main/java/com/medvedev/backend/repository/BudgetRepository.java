package com.medvedev.backend.repository;

import com.medvedev.backend.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    @Query("SELECT b FROM Budget b WHERE b.user.userId = :userId")
    List<Budget> findByUserId(@Param("userId") Integer userId);

    List<Budget> findByUserIdAndStartDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);


    Optional<Budget> findByUserIdAndCategory_Id(Integer userId, Integer categoryId);
}
