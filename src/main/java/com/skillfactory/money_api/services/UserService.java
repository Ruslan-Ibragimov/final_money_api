package com.skillfactory.money_api.services;
import java.math.BigDecimal;
import java.util.Map;

public interface UserService {
    Map<String, Object> getBalance(Long userId);
    Map<String, Object> takeMoney(Long userId, BigDecimal amount);
    Map<String, Object> putMoney(Long userId, BigDecimal amount);
}
