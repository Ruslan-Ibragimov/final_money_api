package com.skillfactory.money_api.services;

import com.skillfactory.money_api.entities.User;
import com.skillfactory.money_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
}
