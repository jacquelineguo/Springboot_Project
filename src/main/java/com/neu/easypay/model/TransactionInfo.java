package com.neu.easypay.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode
public class TransactionInfo {
    private String transactionId;
    private String userId;
    private LocalDateTime transactionTime;
    private double paymentAmount;
    private String cardType;
    // status: successful, failed, pending, fully refunded, partially refunded
    private String transactionStatus;
    private String payerId;
    private String transactionType;
}