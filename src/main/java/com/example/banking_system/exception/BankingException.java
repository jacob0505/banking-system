package com.example.banking_system.exception;

public class BankingException extends RuntimeException {
    
    private String errorCode;
    
    public BankingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}