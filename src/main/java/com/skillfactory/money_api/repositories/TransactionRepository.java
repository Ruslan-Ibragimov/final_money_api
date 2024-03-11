package com.skillfactory.money_api.repositories;

import com.skillfactory.money_api.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndTimestampBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUserIdAndTimestampAfter(Long userId, LocalDateTime startDate);

    List<Transaction> findByUserIdAndTimestampBefore(Long userId, LocalDateTime endDate);
}
