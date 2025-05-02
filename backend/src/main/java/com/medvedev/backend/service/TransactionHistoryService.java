package com.medvedev.backend.service;

import com.medvedev.backend.dto.TransactionHistoryDTO;
import com.medvedev.backend.entity.TransactionHistory;
import com.medvedev.backend.repository.TransactionHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public Page<TransactionHistoryDTO> getTransactionHistory(Integer userId, Integer accountId, Integer categoryId,
                                                             LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<TransactionHistory> historyPage = transactionHistoryRepository.findFilteredHistory(
                userId, accountId, categoryId, startDate, endDate, pageable);

        return historyPage.map(this::convertToDTO); // Convert entities to DTOs
    }

    private TransactionHistoryDTO convertToDTO(TransactionHistory history) {
        return new TransactionHistoryDTO(history.getId(), history.getUser().getName(),
                history.getAccount().getAccountType().toString(),
                history.getCategory().getName(), history.getPaymentMethod().getMethodName(),
                history.getQuantity(), history.getPrice(), history.getAmount(),
                history.getTransactionDate(), history.getDescription());
    }

    public Page<TransactionHistoryDTO> getHistoryByUser(Integer userId, Pageable pageable) {
        Page<TransactionHistory> historyPage = transactionHistoryRepository.findByUserId(userId, pageable);
        return historyPage.map(this::convertToDTO);
    }

    public Page<TransactionHistoryDTO> getHistoryByAccount(Integer accountId, Pageable pageable) {
        Page<TransactionHistory> historyPage = transactionHistoryRepository.findByAccountId(accountId, pageable);
        return historyPage.map(this::convertToDTO);
    }


    public Page<TransactionHistoryDTO> getFilteredHistory(Integer userId, Integer accountId, Integer categoryId,
                                                          LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<TransactionHistory> historyPage = transactionHistoryRepository.findFilteredHistory(userId, accountId, categoryId, startDate, endDate, pageable);
        return historyPage.map(this::convertToDTO);
    }

}
