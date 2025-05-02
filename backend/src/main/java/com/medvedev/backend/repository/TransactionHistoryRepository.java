package com.medvedev.backend.repository;

import com.medvedev.backend.entity.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

    @Query("SELECT t FROM TransactionHistory t WHERE " +
            "(:userId IS NULL OR t.user.userId = :userId) " +
            "AND (:accountId IS NULL OR t.account.id = :accountId) " +
            "AND (:categoryId IS NULL OR t.category.id = :categoryId) " +
            "AND (:startDate IS NULL OR t.transactionDate >= :startDate) " +
            "AND (:endDate IS NULL OR t.transactionDate <= :endDate)")
    Page<TransactionHistory> findFilteredHistory(@Param("userId") Integer userId,
                                                 @Param("accountId") Integer accountId,
                                                 @Param("categoryId") Integer categoryId,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate,
                                                 Pageable pageable);

    Page<TransactionHistory> findByUserId(Integer userId, Pageable pageable);

    Page<TransactionHistory> findByAccountId(Integer accountId, Pageable pageable);

}

