package com.medvedev.backend.controller;

import com.medvedev.backend.entity.Account;
import com.medvedev.backend.enums.AccountType;
import com.medvedev.backend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "Show the account types")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved account types")
    @ApiResponse(responseCode = "404", description = "Account type not found")
    @GetMapping("/types")
    public ResponseEntity<List<String>> getAccountTypes() {
        List<String> accountTypes = Arrays.stream(AccountType.values()).map(Enum::name).toList();
        return ResponseEntity.ok(accountTypes);
    }

    @Operation(summary = "Create a new account")
    @ApiResponse(responseCode = "201", description = "Successfully created a new account")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account savedAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    @Operation(summary = "Retrieve accounts by user ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved accounts for a user")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Account>> getAccountByUserId(@PathVariable Integer userId) {
        List<Account> accounts = accountService.getAccountByUserId(userId);
        return ResponseEntity.ok(accounts.isEmpty()
                ? Collections.emptyList()
                : accounts);
    }

    @Operation(summary = "Update an account")
    @ApiResponse(responseCode = "200", description = "Successfully updated the account")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Integer id, @RequestBody Account updatedAccount) {
        Account updated = accountService.updateAccount(id, updatedAccount);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete an account")
    @ApiResponse(responseCode = "204", description = "Successfully deleted the account")
    @ApiResponse(responseCode = "404", description = "Accout not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Account> deleteAccount(@PathVariable Integer id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
