package com.medvedev.backend.controller;


import com.medvedev.backend.entity.AccountsHistory;
import com.medvedev.backend.service.AccountHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-history")
@RequiredArgsConstructor
public class AccountHistoryController {

    private final AccountHistoryService accountHistoryService;

    @Operation(summary = "Retrieve a history by account")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved a history by account")
    @ApiResponse(responseCode = "404", description = "History not found")
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<AccountsHistory>> getHistoryByAccount(@PathVariable Integer accountId) {
        List<AccountsHistory> history = accountHistoryService.getHistoryByAccount(accountId);
        return history.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(history);
    }

    @Operation(summary = "Create a new account history", description = "Registers a new account history entry")
    @ApiResponse(responseCode = "201", description = "Account history entry successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping
    public ResponseEntity<AccountsHistory> addAccountHistory(@Valid @RequestBody AccountsHistory accountHistory) {
        AccountsHistory savedHistory = accountHistoryService.addAccountHistory(accountHistory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHistory);
    }

    @Operation(summary = "Delete an account history", description = "Deletes the account history by account ID.")
    @ApiResponse(responseCode = "204", description = "Account history successfully deleted")
    @ApiResponse(responseCode = "404", description = "Account history not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountHistory(@PathVariable Integer id) {
        accountHistoryService.deleteAccountHistory(id);
        return ResponseEntity.noContent().build();
    }
}
