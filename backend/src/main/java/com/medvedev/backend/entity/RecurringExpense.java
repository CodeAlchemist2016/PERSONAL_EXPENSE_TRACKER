package com.medvedev.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recurringexpenses", indexes = {
        @Index(name = "idx_user_id", columnList = "User_Id"),
        @Index(name = "idx_category_id", columnList = "Category_Id"),
        @Index(name = "idx_start_date", columnList = "Start_Date"),
        @Index(name = "idx_frequency", columnList = "Frequency")
})
public class RecurringExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecurringExpense_Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "Category_Id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "Account_Id", nullable = false)
    private Account account;

    @Column(name = "Amount", precision = 38, scale = 2, nullable = false)
    @Min(value = 0, message = "Expense amount must be non-negative")
    private BigDecimal amount;

    @Column(name = "Start_Date", nullable = false)
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Frequency", nullable = false)
    private Frequency frequency;

    @Column(name = "Next_Execution_Date")
    private LocalDate nextExecutionDate;

    @Column(name = "Description")
    private String description;
}
