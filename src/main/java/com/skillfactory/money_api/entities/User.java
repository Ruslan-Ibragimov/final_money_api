package com.skillfactory.money_api.entities;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal balance;

    public User() {
    }

    public User(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }


    public User setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }
}
