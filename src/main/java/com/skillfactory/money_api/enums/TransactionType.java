package com.skillfactory.money_api.enums;

public enum TransactionType {
    DEPOSIT(0),
    WITHDRAWAL(1),
    TRANSFER(2);

    private final int value;

    TransactionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
