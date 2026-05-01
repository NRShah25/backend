package com.financetracker.backend.dto;

import com.financetracker.backend.model.Transaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Category is required")
    private Transaction.Category category;

    private String description;

    @NotNull(message = "Type is required")
    private Transaction.Type type;

    private LocalDateTime transactionDate;
}