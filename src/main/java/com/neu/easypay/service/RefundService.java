package com.neu.easypay.service;

import com.neu.easypay.msg.RefundMsg;

public interface RefundService {
    RefundMsg refund(String transactionId, Double refundAmount);

    RefundMsg cancelRefund(String refundId, String transactionId);
}
