package com.financetracker.backend.controller;

import com.financetracker.backend.dto.TransactionRequest;
import com.financetracker.backend.dto.TransactionResponse;
import com.financetracker.backend.model.Transaction;
import com.financetracker.backend.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        TransactionResponse response = transactionService
                .createTransaction(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<TransactionResponse> transactions = transactionService
                .getUserTransactions(userDetails.getUsername());
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        transactionService.deleteTransaction(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<BigDecimal> getMonthlySummary(
            @RequestParam Transaction.Type type,
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetails userDetails) {

        BigDecimal total = transactionService.getTotalByTypeAndMonth(
                userDetails.getUsername(), type, year, month);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/report")
public ResponseEntity<List<Map<String, Object>>> getMonthlyReport(
        @RequestParam int year,
        @AuthenticationPrincipal UserDetails userDetails) {

    List<Map<String, Object>> report = transactionService
            .getMonthlyReport(userDetails.getUsername(), year);
    return ResponseEntity.ok(report);
}
}