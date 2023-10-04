package com.neu.easypay.service.impl;

import com.neu.easypay.service.PaymentProcessor;
import com.neu.easypay.model.PaymentType;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessorFactory {

  @Resource
  CreditCardPaymentProcessor creditCardPaymentProcessor;

  @Resource
  DebitCardPaymentProcessor debitCardPaymentProcessor;

  public PaymentProcessor getPaymentProcessor(PaymentType type) {
    if (type == PaymentType.CREDIT) {
      return creditCardPaymentProcessor;
    }
    if (type == PaymentType.DEBIT) {
      return debitCardPaymentProcessor;
    }
    throw new IllegalArgumentException("Payment type " + type + " is not supported.");
  }

}
