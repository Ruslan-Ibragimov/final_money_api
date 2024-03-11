package com.skillfactory.money_api;

import com.skillfactory.money_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class Controller {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/balance")
    public Map<String, Object> getBalance(@PathVariable Long userId) {
        return userService.getBalance(userId);
    }

    @PostMapping("/{userId}/take-money")
    public Map<String, Object> takeMoney(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        return userService.takeMoney(userId, amount);
    }

    @PostMapping("/{userId}/put-money")
    public Map<String, Object> putMoney(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        return userService.putMoney(userId, amount);
    }
}
