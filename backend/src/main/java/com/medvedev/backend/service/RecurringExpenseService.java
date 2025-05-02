package com.medvedev.backend.service;

import com.medvedev.backend.entity.RecurringExpense;
import com.medvedev.backend.entity.Transaction;
import com.medvedev.backend.repository.RecurringExpensesRepository;
import com.medvedev.backend.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringExpenseService {

    private final RecurringExpensesRepository recurringExpensesRepository;
    private final TransactionRepository transactionRepository;

    public List<RecurringExpense> getRecurringExpensesByUser(Integer userId) {
        List<RecurringExpense> expenses = recurringExpensesRepository.findByUserId(userId);
        if (expenses.isEmpty()) {
            throw new EntityNotFoundException("No recurring expenses found for user ID: " + userId);
        }
        return expenses;
    }


    public RecurringExpense addRecurringExpense(RecurringExpense recurringExpense) {
        return recurringExpensesRepository.save(recurringExpense);
    }

    public void deleteRecurringExpense(Integer id) {
        RecurringExpense expense = recurringExpensesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurring expense not found for ID: " + id));
        recurringExpensesRepository.delete(expense);
    }


    public RecurringExpense updateRecurringExpense(Integer id, @Valid RecurringExpense updatedExpense) {
        RecurringExpense existingExpense = recurringExpensesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurring expense not found for ID: " + id));

        // Update fields
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setStartDate(updatedExpense.getStartDate());
        existingExpense.setFrequency(updatedExpense.getFrequency());

        return recurringExpensesRepository.save(existingExpense);
    }

    @Scheduled(cron = "0 0 1 * * ?")  // Runs on the 1st of every month
    public void processRecurringExpenses() {
        List<RecurringExpense> expenses = recurringExpensesRepository.findAll();
        for (RecurringExpense expense : expenses) {
            if (expense.getNextExecutionDate().isBefore(LocalDate.now())) {
                Transaction transaction = new Transaction();
                transaction.setAccount(expense.getAccount());
                transaction.setAmount(expense.getAmount());
                transaction.setDescription(expense.getDescription());
                transactionRepository.save(transaction);

                switch (expense.getFrequency()) {
                    case MONTHLY -> expense.setNextExecutionDate(expense.getNextExecutionDate().plusMonths(1));
                    case WEEKLY -> expense.setNextExecutionDate(expense.getNextExecutionDate().plusWeeks(1));
                    default -> throw new IllegalStateException("Unsupported frequency: " + expense.getFrequency());
                }
                recurringExpensesRepository.save(expense);
            }
        }
        System.out.println("Recurring expenses processed successfully!");
    }

}
