package com.financetracker.backend.controller;

import com.financetracker.backend.dto.BudgetRequest;
import com.financetracker.backend.dto.BudgetResponse;
import com.financetracker.backend.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BudgetController {

    private final BudgetService budgetService;
    private static final Logger log = LoggerFactory.getLogger(BudgetController.class);

    @PostMapping
    public ResponseEntity<BudgetResponse> createOrUpdateBudget(
            @Valid @RequestBody BudgetRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        BudgetResponse response = budgetService
                .createOrUpdateBudget(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getMonthlyBudgets(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<BudgetResponse> budgets = budgetService
                .getMonthlyBudgets(userDetails.getUsername(), year, month);
        return ResponseEntity.ok(budgets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        budgetService.deleteBudget(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<BudgetResponse>> getBudgetAlerts(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<BudgetResponse> alerts = budgetService
                .checkBudgetAlerts(userDetails.getUsername(), year, month);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/trigger-alerts")
    public ResponseEntity<String> triggerAlerts(
            @AuthenticationPrincipal UserDetails userDetails) {
        budgetService.checkBudgetAlerts(
                userDetails.getUsername(),
                java.time.LocalDateTime.now().getYear(),
                java.time.LocalDateTime.now().getMonthValue())
                .forEach(alert -> log.info("Alert: {}", alert));
        return ResponseEntity.ok("Alert check triggered - check server logs");
    }
}