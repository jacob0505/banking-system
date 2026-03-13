package com.example.banking_system.controller;

import com.example.banking_system.model.Account;

import com.example.banking_system.model.Transaction;
import com.example.banking_system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/accounts")
@Validated
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public Account createAccount(@RequestParam @Size(min = 2, max = 50, 
        message = "Owner name must be between 2 and 50 characters") String ownerName) {
        return accountService.createAccount(ownerName);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/{id}/deposit")
    public Account deposit(@PathVariable Long id, @RequestParam double amount) {
        return accountService.deposit(id, amount);
    }

    @PostMapping("/{id}/withdraw")
    public Account withdraw(@PathVariable Long id, @RequestParam double amount) {
        return accountService.withdraw(id, amount);
    }

    @PostMapping("/transfer")
    public Account transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam double amount) {
        return accountService.transfer(fromId, toId, amount);
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> getTransactionHistory(@PathVariable Long id) {
        return accountService.getTransactionHistory(id);
    }
}