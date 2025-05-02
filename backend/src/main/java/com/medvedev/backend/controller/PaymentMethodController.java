package com.medvedev.backend.controller;

import com.medvedev.backend.entity.PaymentMethod;
import com.medvedev.backend.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Operation(summary = "Retrieve all payment methods")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all payment methods")
    @ApiResponse(responseCode = "404", description = "Payment methods not found")
    @GetMapping
    public ResponseEntity<Page<PaymentMethod>> getAllPaymentMethods(Pageable pageable) {
        Page<PaymentMethod> paymentMethods = paymentMethodService.getAllPaymentMethods(pageable);
        return ResponseEntity.ok(paymentMethods);
    }

    @Operation(summary = "Retrieve a payment method by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payment method by ID")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable Integer id) {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
        return ResponseEntity.ok(paymentMethod);
    }

    @Operation(summary = "Retrieve a payment method by name")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payment method by name")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    @GetMapping("/name/{methodName}")
    public ResponseEntity<PaymentMethod> getPaymentMethodByName(@PathVariable String methodName) {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodByName(methodName);
        return ResponseEntity.ok(paymentMethod);
    }

    @Operation(summary = "Create a new payment method", description = "Registers a new payment method entry")
    @ApiResponse(responseCode = "201", description = "Payment method entry successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping("/create")
    public ResponseEntity<PaymentMethod> createPaymentMethod(@Valid @RequestBody PaymentMethod paymentMethod) {
        PaymentMethod savedPaymentMethod = paymentMethodService.createPaymentMethod(paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentMethod);
    }

    @Operation(summary = "Update a payment method", description = "Updates an existing payment method based on the provided ID and new details.")
    @ApiResponse(responseCode = "200", description = "Payment method successfully updated")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@PathVariable Integer id,
                                                             @Valid @RequestBody PaymentMethod updatedDetails) {
        PaymentMethod updatePaymentMethod = paymentMethodService.updatePaymentMethod(id,
                updatedDetails);
        return ResponseEntity.ok(updatePaymentMethod);
    }

    @Operation(summary = "Delete a payment method", description = "Deletes the payment method with the specified ID.")
    @ApiResponse(responseCode = "204", description = "Payment method successfully deleted")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Integer id) {
        paymentMethodService.deletePaymentMethodById(id);
        return ResponseEntity.noContent().build();
    }
}
