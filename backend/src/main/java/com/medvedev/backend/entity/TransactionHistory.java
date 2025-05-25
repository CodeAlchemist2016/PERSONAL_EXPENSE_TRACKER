package com.medvedev.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Transaction cannot be null")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Account_Id", nullable = false)
    @NotNull(message = "Account cannot be null")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "Category_Id", nullable = false)
    @NotNull(message = "Category cannot be null")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "PaymentMethod_Id", nullable = false)
    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;

    @Column(name = "Quantity")
    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be at least 0.01")
    private BigDecimal quantity;

    @Column(name = "Price", precision = 10, scale = 2)
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private BigDecimal price;

    @Column(name = "Amount", precision = 10, scale = 2)
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private BigDecimal amount;

    @Column(name = "Transaction_Date", updatable = false)
    @NotNull(message = "Transaction date is required")
    private LocalDateTime transactionDate;

    @Column(name = "Description")
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @PrePersist
    protected void onCreate() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }
}
