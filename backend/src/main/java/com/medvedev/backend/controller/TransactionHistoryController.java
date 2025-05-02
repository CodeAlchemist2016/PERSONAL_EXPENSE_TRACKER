package com.medvedev.backend.controller;

import com.medvedev.backend.dto.TransactionHistoryDTO;
import com.medvedev.backend.service.TransactionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transaction-history")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
        this.transactionHistoryService = transactionHistoryService;
    }

    @Operation(summary = "Get transaction history by user ID", description = "Fetches paginated transaction history for a given user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TransactionHistoryDTO>> getHistoryByUser(
            @PathVariable Integer userId,
            @ParameterObject Pageable pageable) {
        Page<TransactionHistoryDTO> history = transactionHistoryService.getHistoryByUser(userId, pageable);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Get transaction history by account ID", description = "Fetches paginated transaction history for a specific account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<TransactionHistoryDTO>> getHistoryByAccount(
            @PathVariable Integer accountId,
            @ParameterObject Pageable pageable) {
        Page<TransactionHistoryDTO> history = transactionHistoryService.getHistoryByAccount(accountId, pageable);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Filter transaction history", description = "Fetches paginated transaction history based on filters like user, account, category, and date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/filter")
    public ResponseEntity<Page<TransactionHistoryDTO>> getHistoryByFilters(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<TransactionHistoryDTO> history = transactionHistoryService.getFilteredHistory(userId, accountId, categoryId, startDate, endDate, pageable);
        return ResponseEntity.ok(history);
    }
}