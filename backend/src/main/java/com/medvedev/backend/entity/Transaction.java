package com.medvedev.backend.entity;

import com.medvedev.backend.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "transactions", indexes = {
        @Index(name = "idx_user_id", columnList = "User_Id"),
        @Index(name = "idx_account_id", columnList = "Account_Id"),
        @Index(name = "idx_category_id", columnList = "Category_Id"),
        @Index(name = "idx_payment_method_id", columnList = "PaymentMethod_Id"),
        @Index(name = "idx_transaction_date", columnList = "Transaction_Date")
})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Transaction_Id")
    private Integer id;

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

    @Column(name = "Quantity", nullable = false)
    private BigDecimal quantity = BigDecimal.valueOf(1);

    @Column(name = "Price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "Amount", precision = 10, scale = 2, updatable = false, nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "Transaction_Date", updatable = false)
    private LocalDateTime transactionDate;

    @Column(name = "Description")
    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<AccountsHistory> accountsHistory;

    @PrePersist
    protected void onCreate() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }
}
