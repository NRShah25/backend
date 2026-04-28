package com.financetracker.backend.dto;

import com.financetracker.backend.model.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private String category;
    private String description;
    private String type;
    private LocalDateTime date;

    public static TransactionResponse fromEntity(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setCategory(transaction.getCategory().name());
        response.setDescription(transaction.getDescription());
        response.setType(transaction.getType().name());
        response.setDate(transaction.getDate());
        return response;
    }
}