package com.medvedev.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RecurringExpenseDTO {
    private Integer id;
    private String description;
    private BigDecimal amount;
    private String frequency; // "Monthly", "Yearly"
    private LocalDate startDate;
    private LocalDate nextDueDate;
    private String categoryName; // Derived from Category
}

