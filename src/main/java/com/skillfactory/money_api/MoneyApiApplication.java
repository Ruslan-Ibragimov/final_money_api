package com.skillfactory.money_api;

import com.skillfactory.money_api.entities.User;
import com.skillfactory.money_api.repositories.UserRepository;
import com.skillfactory.money_api.services.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class MoneyApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(MoneyApiApplication.class, args);

	}

}
