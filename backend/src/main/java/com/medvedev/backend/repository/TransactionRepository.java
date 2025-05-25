package com.medvedev.backend.repository;

import com.medvedev.backend.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId")
    Page<Transaction> findByAccountId(@Param("accountId") Integer accountId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.user.userId = :userId")
    Page<Transaction> findByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.id = :accountId")
    int countTransactionsByAccountId(@Param("accountId") Integer accountId);

    @Query("SELECT t FROM Transaction t")
    List<Transaction> getAllTransactions();

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByTransactionDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Transaction> findByUserIdAndTransactionDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);


    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.userId = :userId AND t.category.id = :categoryId AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal findTotalSpentByUserAndCategory(@Param("userId") Integer userId,
                                               @Param("categoryId") Integer categoryId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
}
