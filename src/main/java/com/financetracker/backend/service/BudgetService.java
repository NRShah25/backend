package com.financetracker.backend.service;

import com.financetracker.backend.dto.BudgetRequest;
import com.financetracker.backend.dto.BudgetResponse;
import com.financetracker.backend.model.Budget;
import com.financetracker.backend.model.Transaction;
import com.financetracker.backend.model.User;
import com.financetracker.backend.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserService userService;
    private final TransactionService transactionService;

    public BudgetResponse createOrUpdateBudget(BudgetRequest request,
            String email) {
        User user = userService.findByEmail(email);

        Budget budget = budgetRepository
                .findByUserAndCategoryAndYearAndMonth(
                        user,
                        request.getCategory(),
                        request.getYear(),
                        request.getMonth())
                .orElse(new Budget());

        budget.setUser(user);
        budget.setCategory(request.getCategory());
        budget.setLimitAmount(request.getLimitAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        Budget saved = budgetRepository.save(budget);

        BigDecimal spent = transactionService.getTotalByTypeAndCategoryAndMonth(
                email,
                Transaction.Type.EXPENSE,
                request.getCategory(),
                request.getYear(),
                request.getMonth());
        return BudgetResponse.fromEntity(saved, spent);
    }

    public List<BudgetResponse> getMonthlyBudgets(String email,
            int year, int month) {
        User user = userService.findByEmail(email);

        return budgetRepository.findByUserAndYearAndMonth(user, year, month)
                .stream()
                .map(budget -> {
                    BigDecimal spent = transactionService
                            .getTotalByTypeAndCategoryAndMonth(
                                    email,
                                    Transaction.Type.EXPENSE,
                                    budget.getCategory(),
                                    year,
                                    month);
                    return BudgetResponse.fromEntity(budget, spent);
                })
                .collect(Collectors.toList());
    }

    public void deleteBudget(Long id, String email) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        budgetRepository.delete(budget);
    }

    public List<BudgetResponse> checkBudgetAlerts(String email,
            int year, int month) {
        return getMonthlyBudgets(email, year, month)
                .stream()
                .filter(b -> b.getStatus().equals("WARNING")
                        || b.getStatus().equals("EXCEEDED"))
                .collect(Collectors.toList());
    }
}