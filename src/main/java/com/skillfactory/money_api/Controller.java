package com.skillfactory.money_api;

import com.skillfactory.money_api.entities.Transaction;
import com.skillfactory.money_api.services.OperationsService;
import com.skillfactory.money_api.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class Controller {

    @Autowired
    private OperationsService userService;

    @GetMapping("/{userId}/balance")
    public Map<String, Object> getBalance(@PathVariable Long userId) {
        return userService.getBalance(userId);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getOperationList(
            @RequestParam Long userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        LocalDateTime startDateTime = DateUtils.parseStringToLocalDateTime(startDate);
        LocalDateTime endDateTime = DateUtils.parseStringToLocalDateTime(endDate);
        List<Transaction> transactions = userService.getOperationList(userId, startDateTime, endDateTime);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/{userId}/take-money")
    public Map<String, Object> takeMoney(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        return userService.takeMoney(userId, amount);
    }

    @PostMapping("/{userId}/put-money")
    public Map<String, Object> putMoney(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        return userService.putMoney(userId, amount);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transferMoney(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam BigDecimal amount
    ) {
        try {
            Map<String, Object> result = userService.transferMoney(senderId, receiverId, amount);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
