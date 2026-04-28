package com.financetracker.backend.repository;

import com.financetracker.backend.model.Budget;
import com.financetracker.backend.model.Transaction;
import com.financetracker.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserAndYearAndMonth(User user, int year, int month);

    Optional<Budget> findByUserAndCategoryAndYearAndMonth(User user,
            Transaction.Category category, int year, int month);

    List<Budget> findByUser(User user);
}