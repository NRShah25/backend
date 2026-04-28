package com.financetracker.backend.repository;

import com.financetracker.backend.model.Transaction;
import com.financetracker.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserOrderByDateDesc(User user);

    List<Transaction> findByUserAndCategory(User user,
            Transaction.Category category);

    List<Transaction> findByUserAndType(User user,
            Transaction.Type type);

    List<Transaction> findByUserAndDateBetween(User user,
            LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE t.user = :user AND t.type = :type " +
            "AND t.date BETWEEN :start AND :end")
    BigDecimal sumByUserAndTypeAndDateBetween(User user,
            Transaction.Type type,
            LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE t.user = :user AND t.type = :type " +
            "AND t.category = :category " +
            "AND t.date BETWEEN :start AND :end")
    BigDecimal sumByUserAndTypeAndCategoryAndDateBetween(User user,
            Transaction.Type type,
            Transaction.Category category,
            LocalDateTime start,
            LocalDateTime end);
}