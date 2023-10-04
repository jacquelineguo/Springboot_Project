package com.neu.easypay.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.neu.easypay.model.TransactionInfo;

public interface ITransactionService {
    
    List<TransactionInfo> getAllTransactions(String userId);

    TransactionInfo getByTransactionID(String userId, String transactionId);

    List<TransactionInfo> 
    getByDate(String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<TransactionInfo> getByStatus(String userId, String transactionStatus);
    
    List<TransactionInfo> getByTransactionIDandStatus(String transactionId, String transactionStatus);
}
