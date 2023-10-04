package com.neu.easypay.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.neu.easypay.mapper.TransactionMapper;
import com.neu.easypay.model.TransactionInfo;
import com.neu.easypay.service.ITransactionService;


@Service
public class TransactionServiceImpl implements ITransactionService{
    
    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public List<TransactionInfo> getAllTransactions(String userId) {
        return transactionMapper.getAllTransactions(userId);
    }

    @Override
    @GetMapping
    public TransactionInfo getByTransactionID(String userId, String transactionId) {
        return transactionMapper.getTransactionByID(userId, transactionId);
    }

    @Override
    public List<TransactionInfo> 
    getByDate(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionMapper.getByDate(userId, startDate, endDate);
    }

    @Override
    public List<TransactionInfo>
    getByStatus(String userId, String transactionStatus) {
        return transactionMapper.getByStatus(userId, transactionStatus);
    }

    @Override
    public List<TransactionInfo> getByTransactionIDandStatus(String transactionID, String transactionStatus) {
        return transactionMapper.getByTransactionIDandStatus(transactionID, transactionStatus);
    }
}