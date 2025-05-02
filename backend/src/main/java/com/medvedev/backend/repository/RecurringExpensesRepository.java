package com.medvedev.backend.repository;

import com.medvedev.backend.entity.RecurringExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringExpensesRepository extends JpaRepository<RecurringExpense, Integer> {
    @Query("SELECT re FROM RecurringExpense re WHERE re.user.userId = :userId")
    List<RecurringExpense> findByUserId(@Param("userId") Integer userId);
}

