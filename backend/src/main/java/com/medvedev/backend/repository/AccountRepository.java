package com.medvedev.backend.repository;

import com.medvedev.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT a from Account a where a.user.userId = :userId")
    List<Account> findByUserId(@Param("userId") Integer user);
}


