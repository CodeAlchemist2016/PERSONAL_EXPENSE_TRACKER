package com.medvedev.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class CreateTransactionRequest {

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Account ID is required")
    private Integer accountId;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @NotNull
    private Integer paymentMethodId;

    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    private BigDecimal quantity = BigDecimal.valueOf(1);

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}
