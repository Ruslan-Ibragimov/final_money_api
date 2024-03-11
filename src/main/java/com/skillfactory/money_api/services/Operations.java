package com.skillfactory.money_api.services;
import com.skillfactory.money_api.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface Operations {
    Map<String, Object> getBalance(Long userId);
    Map<String, Object> takeMoney(Long userId, BigDecimal amount);
    Map<String, Object> putMoney(Long userId, BigDecimal amount);
    List<Transaction> getOperationList(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> transferMoney(Long senderId, Long receiverId, BigDecimal amount);
}
