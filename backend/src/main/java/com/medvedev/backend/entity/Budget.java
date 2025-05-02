package com.medvedev.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "budgets", indexes = {
        @Index(name = "idx_user_id", columnList = "User_Id"),
        @Index(name = "idx_category_id", columnList = "Category_Id"),
        @Index(name = "idx_start_date", columnList = "Start_Date"),
        @Index(name = "idx_end_date", columnList = "End_Date")
})
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Budget_Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "Category_Id", nullable = false)
    private Category category;

    @Column(name = "Budget_Limit", precision = 10, scale = 2)
    private BigDecimal budgetLimit;

    @Column(name = "Remaining_Budget", precision = 10, scale = 2)
    private BigDecimal remainingBudget;


    @Column(name = "Start_Date", nullable = false)
    private LocalDate startDate;

    @Column(name = "End_Date", nullable = false)
    private LocalDate endDate;
}
