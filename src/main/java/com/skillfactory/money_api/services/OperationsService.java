package com.skillfactory.money_api.services;

import com.skillfactory.money_api.entities.Transaction;
import com.skillfactory.money_api.entities.User;
import com.skillfactory.money_api.enums.TransactionType;
import com.skillfactory.money_api.repositories.TransactionRepository;
import com.skillfactory.money_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationsService implements Operations {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public Map<String, Object> getBalance(Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                result.put("status", 1);
                result.put("balance", user.getBalance());
            } else {
                result.put("status", -1);
                result.put("message", "User not found");
            }
        } catch (Exception e) {
            result.put("status", -1);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> takeMoney(Long userId, BigDecimal amount) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                BigDecimal newBalance = user.getBalance().subtract(amount);
                if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
                    user.setBalance(newBalance);
                    userRepository.save(user);
                    addTransaction(userId, null, TransactionType.WITHDRAWAL, amount);
                    result.put("status", 1);
                } else {
                    result.put("status", 0);
                    result.put("message", "Insufficient funds");
                }
            } else {
                result.put("status", 0);
                result.put("message", "User not found");
            }
        } catch (Exception e) {
            result.put("status", 0);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> putMoney(Long userId, BigDecimal amount) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                BigDecimal newBalance = user.getBalance().add(amount);
                user.setBalance(newBalance);
                userRepository.save(user);
                addTransaction(userId, null, TransactionType.DEPOSIT, amount);
                result.put("status", 1);
            } else {
                result.put("status", 0);
                result.put("message", "User not found");
            }
        } catch (Exception e) {
            result.put("status", -1);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    public List<Transaction> getOperationList(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            // Если оба значения дат пусты, вернуть все операции пользователя
            return transactionRepository.findByUserId(userId);
        } else if (startDate != null && endDate != null) {
            // Если оба значения дат заполнены, вернуть операции за диапазон дат
            return transactionRepository.findByUserIdAndTimestampBetween(userId, startDate, endDate);
        } else if (startDate != null) {
            // Если заполнено только начальное значение, вернуть операции с этой даты и позже
            return transactionRepository.findByUserIdAndTimestampAfter(userId, startDate);
        } else {
            // Если заполнено только конечное значение, вернуть операции до этой даты
            return transactionRepository.findByUserIdAndTimestampBefore(userId, endDate);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> transferMoney(Long senderId, Long receiverId, BigDecimal amount) {
        Map<String, Object> result = new HashMap<>();

        try {
            Optional<User> optionalSender = userRepository.findById(senderId);
            Optional<User> optionalReceiver = userRepository.findById(receiverId);

            if (optionalSender.isPresent() && optionalReceiver.isPresent()) {
                User sender = optionalSender.get();
                User receiver = optionalReceiver.get();

                // Проверяем, достаточно ли средств у отправителя
                if (sender.getBalance().compareTo(amount) < 0) {
                    result.put("status", 0);
                    result.put("message", "Insufficient funds in the sender's account");
                    return result;
                }

                // Выполняем транзакцию
                BigDecimal newSenderBalance = sender.getBalance().subtract(amount);
                BigDecimal newReceiverBalance = receiver.getBalance().add(amount);

                sender.setBalance(newSenderBalance);
                receiver.setBalance(newReceiverBalance);

                userRepository.save(sender);
                userRepository.save(receiver);

                addTransaction(sender.getId(), receiver.getId(), TransactionType.TRANSFER, amount);

                result.put("status", 1);
                result.put("message", "Money transferred successfully");
            } else {
                result.put("status", 0);
                result.put("message", "Invalid sender or receiver ID");
            }
        } catch (Exception e) {
            result.put("status", 0);
            result.put("message", e.getMessage());
        }

        return result;
    }

    @Transactional
    public void addTransaction(Long userId, Long receiverId, TransactionType type, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setReceiverId(receiverId);
        transaction.setOperationType(type.getValue());
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}
