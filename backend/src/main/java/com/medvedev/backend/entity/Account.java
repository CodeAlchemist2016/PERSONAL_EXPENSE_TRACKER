package com.medvedev.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.medvedev.backend.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "accounts", indexes = {
        @Index(name = "idx_user_id", columnList = "User_Id"),
        @Index(name = "idx_account_type", columnList = "Account_Type")
})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Account_Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    @JsonBackReference
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "Account_Type", nullable = false)
    private AccountType accountType;

    @Column(name = "Balance", precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "Max_Spending", precision = 10, scale = 2)
    private BigDecimal maxSpending = BigDecimal.ZERO; // ✅ Default to 0

    @Column(name = "Total_Transactions")
    private Integer totalTransactions = 0; // ✅ Default to 0

}
