package com.medvedev.backend.dto;

import com.medvedev.backend.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private ReportType reportType;
    private Integer userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal budgetLimit;
    private BigDecimal remainingBudget;
    private Map<String, Object> additionalData;
}
