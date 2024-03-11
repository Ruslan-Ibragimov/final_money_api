package com.skillfactory.money_api.services;

import com.skillfactory.money_api.entities.Transaction;
import com.skillfactory.money_api.entities.User;
import com.skillfactory.money_api.repositories.TransactionRepository;
import com.skillfactory.money_api.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OperationsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private OperationsService operationsService;

    @Test
    void takeMoneyShouldReduceBalanceAndAddTransaction() {
        // Arrange
        Long userId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal amountToTake = BigDecimal.valueOf(50);

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setBalance(initialBalance);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        operationsService.takeMoney(userId, amountToTake);

        // Assert
        assertEquals(initialBalance.subtract(amountToTake), mockUser.getBalance());
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    void putMoneyShouldIncreaseBalanceAndAddTransaction() {
        // Arrange
        Long userId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal amountToPut = BigDecimal.valueOf(50);

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setBalance(initialBalance);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        operationsService.putMoney(userId, amountToPut);

        // Assert
        assertEquals(initialBalance.add(amountToPut), mockUser.getBalance());
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    void transferMoneyShouldTransferMoneyBetweenUsersAndAddTransaction() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal initialSenderBalance = BigDecimal.valueOf(100);
        BigDecimal initialReceiverBalance = BigDecimal.valueOf(50);
        BigDecimal transferAmount = BigDecimal.valueOf(30);

        User mockSender = new User();
        mockSender.setId(senderId);
        mockSender.setBalance(initialSenderBalance);

        User mockReceiver = new User();
        mockReceiver.setId(receiverId);
        mockReceiver.setBalance(initialReceiverBalance);

        Mockito.when(userRepository.findById(senderId)).thenReturn(Optional.of(mockSender));
        Mockito.when(userRepository.findById(receiverId)).thenReturn(Optional.of(mockReceiver));

        // Act
        operationsService.transferMoney(senderId, receiverId, transferAmount);

        // Assert
        assertEquals(initialSenderBalance.subtract(transferAmount), mockSender.getBalance());
        assertEquals(initialReceiverBalance.add(transferAmount), mockReceiver.getBalance());
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
    }
}
