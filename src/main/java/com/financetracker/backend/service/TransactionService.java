package com.financetracker.backend.service;

import com.financetracker.backend.dto.TransactionRequest;
import com.financetracker.backend.dto.TransactionResponse;
import com.financetracker.backend.model.Transaction;
import com.financetracker.backend.model.User;
import com.financetracker.backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public TransactionResponse createTransaction(TransactionRequest request,
            String email) {
        User user = userService.findByEmail(email);

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setCategory(request.getCategory());
        transaction.setDescription(request.getDescription());
        transaction.setType(request.getType());
        transaction.setUser(user);

        Transaction saved = transactionRepository.save(transaction);
        return TransactionResponse.fromEntity(saved);
    }

    public List<TransactionResponse> getUserTransactions(String email) {
        User user = userService.findByEmail(email);
        return transactionRepository.findByUserOrderByDateDesc(user)
                .stream()
                .map(TransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteTransaction(Long id, String email) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        transactionRepository.delete(transaction);
    }

    public BigDecimal getTotalByTypeAndMonth(String email,
            Transaction.Type type, int year, int month) {
        User user = userService.findByEmail(email);
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);

        BigDecimal total = transactionRepository
                .sumByUserAndTypeAndDateBetween(user, type, start, end);

        return total != null ? total : BigDecimal.ZERO;
    }
    public BigDecimal getTotalByTypeAndCategoryAndMonth(String email,
        Transaction.Type type, Transaction.Category category,
        int year, int month) {
    User user = userService.findByEmail(email);
    LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
    LocalDateTime end = start.plusMonths(1).minusSeconds(1);

    BigDecimal total = transactionRepository
            .sumByUserAndTypeAndCategoryAndDateBetween(
                    user, type, category, start, end);

    return total != null ? total : BigDecimal.ZERO;
}
}