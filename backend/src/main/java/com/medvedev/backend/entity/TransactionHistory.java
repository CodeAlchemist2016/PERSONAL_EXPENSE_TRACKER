package com.medvedev.backend.entity;

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
@Table(name = "transactionhistory", indexes = {
        @Index(name = "idx_transaction_id", columnList = "Transaction_Id"),
        @Index(name = "idx_user_id", columnList = "User_Id"),
        @Index(name = "idx_account_id", columnList = "Account_Id"),
        @Index(name = "idx_category_id", columnList = "Category_Id"),
        @Index(name = "idx_payment_method_id", columnList = "PaymentMethod_Id"),
        @Index(name = "idx_transaction_date", columnList = "Transaction_Date")
})
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "History_Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Transaction_Id", nullable = false)
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "Account_Id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "Category_Id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "PaymentMethod_Id", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "Quantity")
    private BigDecimal quantity;

    @Column(name = "Price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "Amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "Transaction_Date", updatable = false)
    private LocalDateTime transactionDate;

    @Column(name = "Description")
    private String description;

    @PrePersist
    protected void onCreate() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }
}
