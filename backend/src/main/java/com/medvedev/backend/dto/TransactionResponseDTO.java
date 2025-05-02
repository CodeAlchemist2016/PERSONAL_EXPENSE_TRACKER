package com.medvedev.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDTO {
    private Integer id;
    private String accountType; // Derived from Account
    private String categoryName; // Derived from Category
    private String userName; // Derived from User
    private BigDecimal amount;
    private BigDecimal quantity;
    private BigDecimal price;
    private String description;
    private LocalDateTime transactionDate;
    private String paymentMethodName; // Derived from PaymentMethod
    private String transactionType;
    private BigDecimal oldBalance;
    private BigDecimal newBalance;
}
