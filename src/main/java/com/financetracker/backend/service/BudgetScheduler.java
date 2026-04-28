package com.financetracker.backend.service;

import com.financetracker.backend.dto.BudgetResponse;
import com.financetracker.backend.model.User;
import com.financetracker.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BudgetScheduler {

    private final UserRepository userRepository;
    private final BudgetService budgetService;
    private final AlertService alertService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkAllUserBudgets() {
        log.info("Running daily budget check...");

        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonthValue();

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            try {
                List<BudgetResponse> alerts = budgetService
                        .checkBudgetAlerts(user.getEmail(), year, month);

                for (BudgetResponse alert : alerts) {
                    double percentage = alert.getSpentAmount()
                            .divide(alert.getLimitAmount(), 2,
                                    java.math.RoundingMode.HALF_UP)
                            .doubleValue() * 100;

                    alertService.sendBudgetAlert(
                            user.getEmail(),
                            alert.getCategory(),
                            alert.getStatus(),
                            percentage);

                    log.info("Alert triggered for user: {} category: {} status: {}",
                            user.getEmail(), alert.getCategory(), alert.getStatus());
                }

            } catch (Exception e) {
                log.error("Error checking budgets for user: {}",
                        user.getEmail(), e);
            }
        }

        log.info("Daily budget check complete.");
    }
}