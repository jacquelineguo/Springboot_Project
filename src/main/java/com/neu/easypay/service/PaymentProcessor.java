package com.neu.easypay.service;

import com.neu.easypay.model.CardInfo;
import com.neu.easypay.model.PaymentProcessingResult;

public interface PaymentProcessor {

  PaymentProcessingResult processPayment(
      String payerId,
      CardInfo cardInfo,
      String userId,
      Double amount);

  String getPaymentType();
}
