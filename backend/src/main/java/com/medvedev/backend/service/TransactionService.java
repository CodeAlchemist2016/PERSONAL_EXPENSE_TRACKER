package com.medvedev.backend.service;

import com.medvedev.backend.dto.CreateTransactionRequest;
import com.medvedev.backend.dto.TransactionResponseDTO;
import com.medvedev.backend.entity.*;
import com.medvedev.backend.enums.TransactionType;
import com.medvedev.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j @Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final ModelMapper modelMapper;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public Optional<List<Transaction>> getTransactionsByUserId(Integer userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.isEmpty() ? Optional.empty() : Optional.of(transactions);
    }

    public Optional<List<Transaction>> getTransactionsByAccountId(Integer accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.isEmpty() ? Optional.empty() : Optional.of(transactions);
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        if (transaction.getAccount() == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        if (transaction.getPrice() == null) {
            transaction.setPrice(BigDecimal.ZERO);
        }

        if (transaction.getAmount() == null) {
            transaction.setAmount(transaction.getQuantity().multiply(transaction.getPrice()));
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        // 🔍 Log to verify data is correctly saved
        System.out.println("Saved Transaction Price: " + savedTransaction.getPrice());

        return savedTransaction;
    }

    public int countTransactionsByAccountId(Integer accountId) {
        return transactionRepository.countTransactionsByAccountId(accountId);
    }

    @Transactional(readOnly = true)
    List<TransactionResponseDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction,
                        TransactionResponseDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponseDTO createTransaction(CreateTransactionRequest request) {

        validateRequestData(request);

        // Fetch related entities
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        // Map request to Transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO);
        transaction.setAmount(transaction.getQuantity().multiply(transaction.getPrice()).setScale(2, RoundingMode.HALF_UP));
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.EXPENSE); // or TransactionType.INCOME, based on logic

        // Save transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Log transaction history immediately after saving
        logTransactionHistory(savedTransaction, "CREATED");

        // 🔍 Log transaction after saving
        System.out.println("Transaction history logged!");

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setId(savedTransaction.getId());
        responseDTO.setAccountType(account.getAccountType().toString());
        responseDTO.setCategoryName(category.getName());
        responseDTO.setUserName(user.getName());
        responseDTO.setAmount(savedTransaction.getAmount());
        responseDTO.setQuantity(savedTransaction.getQuantity());
        responseDTO.setPrice(savedTransaction.getPrice());
        responseDTO.setDescription(savedTransaction.getDescription());
        responseDTO.setTransactionDate(savedTransaction.getTransactionDate());
        responseDTO.setPaymentMethodName(paymentMethod.getMethodName());
        responseDTO.setTransactionType(transaction.getTransactionType() != null ? transaction.getTransactionType().name() : null);
        responseDTO.setOldBalance(account.getBalance());
        responseDTO.setNewBalance(account.getBalance().subtract(transaction.getAmount()));

        return responseDTO;
    }

    @Transactional
    public void logTransactionHistory(Transaction transaction, String changeType) {
        TransactionHistory history = new TransactionHistory();
        history.setTransaction(transaction);
        history.setUser(transaction.getUser());
        history.setAccount(transaction.getAccount());
        history.setCategory(transaction.getCategory());
        history.setPaymentMethod(transaction.getPaymentMethod());
        history.setQuantity(transaction.getQuantity());
        history.setPrice(transaction.getPrice());
        history.setAmount(transaction.getAmount());
        history.setDescription(transaction.getDescription());
        history.setTransactionDate(transaction.getTransactionDate());

        transactionHistoryRepository.save(history);
        System.out.println("Transaction history logged: " + changeType);
    }


    private void validateRequestData(CreateTransactionRequest request) {
        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new IllegalArgumentException("User ID must be provided and greater than 0");
        }
        if (request.getAccountId() == null || request.getAccountId() <= 0) {
            throw new IllegalArgumentException("Account ID must be provided and greater than 0");
        }
        if (request.getCategoryId() == null || request.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Category ID must be provided and greater than 0");
        }
        if (request.getPaymentMethodId() == null || request.getPaymentMethodId() <= 0) {
            throw new IllegalArgumentException("Payment Method ID must be provided and greater than 0");
        }
        if (request.getQuantity() == null || request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }
}
