package com.medvedev.backend.entity;

import com.medvedev.backend.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accountshistory", indexes = {
        @Index(name = "idx_account_id", columnList = "Account_Id"),
        @Index(name = "idx_transaction_id", columnList = "Transaction_Id"),
        @Index(name = "idx_change_date", columnList = "Change_Date"),
        @Index(name = "idx_transaction_type", columnList = "Transaction_Type")
})
public class AccountsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "History_Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Account_Id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "Transaction_Id", nullable = false)
    private Transaction transaction;

    @Column(name = "Old_Balance", precision = 10, scale = 2)
    private BigDecimal oldBalance;

    @Column(name = "New_Balance", precision = 10, scale = 2)
    private BigDecimal newBalance;

    @Column(name = "Change_Amount", precision = 10, scale = 2)
    private BigDecimal changeAmount;

    @Column(name = "Change_Date")
    private LocalDateTime changeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Transaction_Type", nullable = false)
    private TransactionType transactionType;

    @PrePersist
    protected void onCreate() {
        if (this.changeDate == null) {
            this.changeDate = LocalDateTime.now();
        }
    }
}
