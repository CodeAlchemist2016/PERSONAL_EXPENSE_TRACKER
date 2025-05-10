package com.medvedev.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class AccountSummaryDTO {
    private Integer id;
    private String accountType; // E.g., "SAVINGS", "CHECKING"
    private BigDecimal balance;
    private BigDecimal maxSpending; // Optional
    private Integer totalTransactions;
    private String accountNumber;
    private String bankName;
}
