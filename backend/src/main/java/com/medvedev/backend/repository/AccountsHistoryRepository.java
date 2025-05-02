package com.medvedev.backend.repository;

import com.medvedev.backend.entity.AccountsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsHistoryRepository extends JpaRepository<AccountsHistory, Integer> {
    @Query("SELECT ah FROM AccountsHistory ah WHERE ah.account.id = :accountId")
    List<AccountsHistory> findByAccountId(@Param("accountId") Integer accountId);
}

