package com.medvedev.backend.controller;

import com.medvedev.backend.dto.CreateTransactionRequest;
import com.medvedev.backend.dto.TransactionResponseDTO;
import com.medvedev.backend.entity.Transaction;
import com.medvedev.backend.repository.CategoryRepository;
import com.medvedev.backend.repository.TransactionRepository;
import com.medvedev.backend.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Operation(summary = "Retrieve all transactions", description = "Fetches a complete list of transactions across all accounts.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions")
    @ApiResponse(responseCode = "404", description = "No transactions found for user")
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.getAllTransactions();

        // Convert transactions to TransactionResponseDTO
        List<TransactionResponseDTO> responseDTOS = transactions.stream()
                .map(transaction -> new TransactionResponseDTO(
                        transaction.getId(),
                        transaction.getAccount().getAccountType().toString(),
                        transaction.getCategory().getName(),
                        transaction.getUser().getName(),
                        transaction.getAmount(),
                        transaction.getQuantity(),
                        transaction.getPrice(),
                        transaction.getDescription(),
                        transaction.getTransactionDate(),
                        transaction.getPaymentMethod().getMethodName(),
                        transaction.getTransactionType().name(),
                        transaction.getAccount().getBalance(), // Old balance
                        transaction.getAccount().getBalance().subtract(transaction.getPrice()) // New balance
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOS);
    }

    @Operation(summary = "Retrieve transactions by user ID", description = "Fetches transactions that belong to a specific user.")
    @ApiResponse(responseCode = "200", description = "Transactions found")
    @ApiResponse(responseCode = "404", description = "No transactions found for user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable Integer userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Retrieve transactions by account ID", description = "Fetches transactions that belong to a specific account.")
    @ApiResponse(responseCode = "200", description = "Transactions found")
    @ApiResponse(responseCode = "404", description = "No transactions found for account")
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Integer accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Create a new transaction", description = "Registers a new transaction")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {
        TransactionResponseDTO response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
