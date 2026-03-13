package com.example.banking_system.repository;

import com.example.banking_system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // 查看某個戶口的所有交易記錄
    List<Transaction> findByFromAccountIdOrToAccountId(Long fromId, Long toId);
}