package com.medvedev.backend.controller;

import com.medvedev.backend.entity.RecurringExpense;
import com.medvedev.backend.service.RecurringExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recurring-expenses")
public class RecurringExpenseController {

    private final RecurringExpenseService recurringExpenseService;

    @Operation(summary = "Retrieve a recurring expense by user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved a recurring expense by user")
    @ApiResponse(responseCode = "404", description = "History not found")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecurringExpense>> getRecurringExpensesByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(recurringExpenseService.getRecurringExpensesByUser(userId));
    }

    @Operation(summary = "Update a recurring expense", description = "Updates an existing recurring expense")
    @ApiResponse(responseCode = "200", description = "Recurring expense successfully updated")
    @ApiResponse(responseCode = "404", description = "Recurring expense not found")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PutMapping("/{id}")
    public ResponseEntity<RecurringExpense> updateRecurringExpense(@PathVariable Integer id,
                                                                   @Valid @RequestBody RecurringExpense updatedExpense) {
        RecurringExpense updated = recurringExpenseService.updateRecurringExpense(id, updatedExpense);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Create a new recurring expense", description = "Creates a new recurring expense.")
    @ApiResponse(responseCode = "201", description = "Recurring expense successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping
    public ResponseEntity<RecurringExpense> addRecurringExpense(@Valid @RequestBody RecurringExpense recurringExpense) {
        RecurringExpense savedExpense = recurringExpenseService.addRecurringExpense(recurringExpense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    @Operation(summary = "Delete a recurring expense", description = "Deletes the recurring expense")
    @ApiResponse(responseCode = "204", description = "Recurring expense successfully deleted")
    @ApiResponse(responseCode = "404", description = "Recurring expense not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecurringExpense(@PathVariable Integer id) {
        recurringExpenseService.deleteRecurringExpense(id);
        return ResponseEntity.noContent().build();
    }
}
