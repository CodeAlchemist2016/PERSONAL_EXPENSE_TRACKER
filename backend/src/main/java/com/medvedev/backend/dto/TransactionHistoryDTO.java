package com.medvedev.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryDTO {

    private Integer historyId;
    private String userName;
    private String accountType;
    private String categoryName;
    private String paymentMethodName;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;
}
