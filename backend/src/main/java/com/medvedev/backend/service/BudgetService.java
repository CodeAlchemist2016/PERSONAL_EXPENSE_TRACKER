package com.medvedev.backend.service;

import com.medvedev.backend.entity.Budget;
import com.medvedev.backend.entity.Transaction;
import com.medvedev.backend.repository.BudgetRepository;
import com.medvedev.backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    public Optional<List<Budget>> getBudgetsForUsers(Integer userId) {
        List<Budget> budgets = budgetRepository.findByUserId(userId);
        return budgets.isEmpty() ? Optional.empty() : Optional.of(budgets);
    }

    public boolean isWithinBudget(Transaction transaction) {
        Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndCategory_Id(
                transaction.getUser().getUserId(), transaction.getCategory().getId()
        );

        if (budgetOpt.isEmpty()) {
            return true; // No budget set, transaction is allowed
        }

        Budget budget = budgetOpt.get();
        BigDecimal totalSpent = transactionRepository.findTotalSpentByUserAndCategory(
                transaction.getUser().getUserId(), transaction.getCategory().getId(),
                budget.getStartDate(), budget.getEndDate()
        );

        return totalSpent.add(transaction.getAmount()).compareTo(budget.getBudgetLimit()) <= 0;
    }


    public void updateRemainingBudget(Transaction transaction) {
        Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndCategory_Id(
                transaction.getUser().getUserId(), transaction.getCategory().getId()
        );

        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            BigDecimal newRemainingBudget = budget.getRemainingBudget().subtract(transaction.getAmount());

            budget.setRemainingBudget(newRemainingBudget.max(BigDecimal.ZERO)); // Prevent negative values
            budgetRepository.save(budget);
        }
    }
}
