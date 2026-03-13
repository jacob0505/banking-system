package com.example.banking_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Owner name cannot be empty")
    @Size(min = 2, max = 50, message = "Owner name must be between 2 and 50 characters")
    private String ownerName;

    private String accountNumber;

    @Min(value = 0, message = "Balance cannot be negative")
    private double balance;

    // Getters
    public Long getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setBalance(double balance) { this.balance = balance; }
}