package com.financetracker.backend.dto;

import com.financetracker.backend.model.Budget;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetResponse {

    private Long id;
    private String category;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private int month;
    private int year;
    private String status;

    public static BudgetResponse fromEntity(Budget budget,
            BigDecimal spentAmount) {
        BudgetResponse response = new BudgetResponse();
        response.setId(budget.getId());
        response.setCategory(budget.getCategory().name());
        response.setLimitAmount(budget.getLimitAmount());
        response.setSpentAmount(spentAmount);
        response.setMonth(budget.getMonth());
        response.setYear(budget.getYear());

        BigDecimal remaining = budget.getLimitAmount().subtract(spentAmount);
        response.setRemainingAmount(remaining);

        double percentage = spentAmount
                .divide(budget.getLimitAmount(), 2,
                        java.math.RoundingMode.HALF_UP)
                .doubleValue() * 100;

        if (percentage >= 100) {
            response.setStatus("EXCEEDED");
        } else if (percentage >= 80) {
            response.setStatus("WARNING");
        } else {
            response.setStatus("OK");
        }

        return response;
    }
}