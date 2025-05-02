package com.medvedev.backend.controller;

import com.medvedev.backend.entity.Budget;
import com.medvedev.backend.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private BudgetService budgetService;

    @Operation(summary = "Retrieve all budgets by user ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all budgets by user ID")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Budget>> getAllBudgetsByUserId(@PathVariable Integer userId) {
        return budgetService.getBudgetsForUsers(userId)
                .map(ResponseEntity::ok) // Unwraps the Optional if present
                .orElseGet(() -> ResponseEntity.notFound().build()); // Handles empty case
    }

}

