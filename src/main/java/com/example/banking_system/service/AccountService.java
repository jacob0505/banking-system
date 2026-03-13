package com.example.banking_system.service;

import com.example.banking_system.exception.BankingException;
import com.example.banking_system.model.Account;
import com.example.banking_system.model.Transaction;
import com.example.banking_system.repository.AccountRepository;
import com.example.banking_system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // 開戶
    public Account createAccount(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new BankingException("Owner name cannot be empty", "INVALID_OWNER_NAME");
        }
        Account account = new Account();
        account.setOwnerName(ownerName);
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        account.setBalance(0.0);
        return accountRepository.save(account);
    }

    // 查看所有戶口
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // 查看單一戶口
    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new BankingException(
                    "Account not found with id: " + id, "ACCOUNT_NOT_FOUND"));
    }

    // 存款
    public Account deposit(Long id, double amount) {
        if (amount <= 0) {
            throw new BankingException("Deposit amount must be greater than 0", "INVALID_AMOUNT");
        }
        Account account = getAccount(id);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        saveTransaction(null, id, amount, "DEPOSIT");
        return account;
    }

    // 提款
    public Account withdraw(Long id, double amount) {
        if (amount <= 0) {
            throw new BankingException("Withdraw amount must be greater than 0", "INVALID_AMOUNT");
        }
        Account account = getAccount(id);
        if (account.getBalance() < amount) {
            throw new BankingException(
                "Insufficient balance. Current balance: " + account.getBalance(), 
                "INSUFFICIENT_BALANCE");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        saveTransaction(id, null, amount, "WITHDRAW");
        return account;
    }

    // 轉帳
    public Account transfer(Long fromId, Long toId, double amount) {
        if (amount <= 0) {
            throw new BankingException("Transfer amount must be greater than 0", "INVALID_AMOUNT");
        }
        if (fromId.equals(toId)) {
            throw new BankingException("Cannot transfer to the same account", "SAME_ACCOUNT");
        }
        Account fromAccount = getAccount(fromId);
        Account toAccount = getAccount(toId);
        if (fromAccount.getBalance() < amount) {
            throw new BankingException(
                "Insufficient balance. Current balance: " + fromAccount.getBalance(),
                "INSUFFICIENT_BALANCE");
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        saveTransaction(fromId, toId, amount, "TRANSFER");
        return fromAccount;
    }

    // 查看交易記錄
    public List<Transaction> getTransactionHistory(Long accountId) {
        getAccount(accountId);
        return transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId);
    }

    // 儲存交易記錄
    private void saveTransaction(Long fromId, Long toId, double amount, String type) {
        Transaction transaction = new Transaction();
        transaction.setFromAccountId(fromId);
        transaction.setToAccountId(toId);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}