package com.medvedev.backend.service;

import com.medvedev.backend.dto.ReportDTO;
import com.medvedev.backend.entity.Budget;
import com.medvedev.backend.entity.Transaction;
import com.medvedev.backend.enums.ReportType;
import com.medvedev.backend.enums.TransactionType;
import com.medvedev.backend.repository.BudgetRepository;
import com.medvedev.backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    public ReportDTO generateReport(ReportType reportType, Integer userId, LocalDate startDate, LocalDate endDate) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportType(reportType);
        reportDTO.setUserId(userId);
        reportDTO.setStartDate(startDate);
        reportDTO.setEndDate(endDate);

        switch (reportType) {
            case TRANSACTION:
                reportDTO = generateTransactionReport(userId, startDate, endDate);
                break;
            case BUDGET:
                reportDTO = generateBudgetReport(userId, startDate, endDate);
                break;
            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);
        }

        return reportDTO;
    }

    private ReportDTO generateTransactionReport(Integer userId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionDateBetween(userId, startDate, endDate);
        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReportDTO(ReportType.TRANSACTION, userId, startDate, endDate, totalIncome, totalExpenses, null, null, null);
    }

    private ReportDTO generateBudgetReport(Integer userId, LocalDate startDate, LocalDate endDate) {
        List<Budget> budgets = budgetRepository.findByUserIdAndStartDateBetween(userId, startDate, endDate);
        BigDecimal totalBudgetLimit = budgets.stream()
                .map(Budget::getBudgetLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRemainingBudget = budgets.stream()
                .map(Budget::getRemainingBudget)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReportDTO(ReportType.BUDGET, userId, startDate, endDate, null, null, totalBudgetLimit, totalRemainingBudget, null);
    }
}

