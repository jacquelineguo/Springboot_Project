package com.neu.easypay.service.impl;

import com.neu.easypay.mapper.PaymentDetailsMapper;
import com.neu.easypay.mapper.UserAccountMapper;
import com.neu.easypay.model.CardInfo;
import com.neu.easypay.model.PaymentDetail;
import com.neu.easypay.model.PaymentProcessingResult;
import com.neu.easypay.model.UserAccount;
import com.neu.easypay.service.PaymentProcessor;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;
import lombok.Data;

@Data
public abstract class PaymentProcessorWithDbPersist implements PaymentProcessor {

  static final String COMPLETE = "complete";
  static final String FAILED = "failure";

  @Resource
  PaymentDetailsMapper paymentDetailsMapper;

  @Resource
  UserAccountMapper userAccountMapper;

  PaymentProcessingResult persistPaymentDetail(String transactionStatus,
      String transactionStatusMessage,
      String payerId,
      CardInfo cardInfo,
      Double amount,
      String userId) {

    UUID transactionId = UUID.randomUUID();
    Date transactionTime = new Date();

    PaymentDetail paymentDetail = PaymentDetail.builder()
        .transactionId(transactionId.toString())
        .transactionTime(transactionTime)
        .transactionStatus(transactionStatus)
        .transactionType("payment")
        .transactionStatusMessage(transactionStatusMessage)
        .payerId(payerId)
        .payerName(cardInfo.getCardHolderName())
        .payerZipcode(cardInfo.getCardHolderZipcode())
        .cardNumber(cardInfo.getCardNumber())
        .cardType(getPaymentType())
        .cardExpDate(cardInfo.getCardExpDate())
        .cardCvv(cardInfo.getCardCvv())
        .paymentAmount(amount)
        .refundAmount(0.0)
        .userId(userId)
        .build();

    paymentDetailsMapper.insertPaymentDetail(paymentDetail);

    // Update merchant balance
    UserAccount userAccount = userAccountMapper.getByUserUUID(userId);
    Double newBalance = userAccount.getBalance() + amount;
    userAccountMapper.update(userId, newBalance);

    return PaymentProcessingResult.builder()
        .transactionId(transactionId.toString())
        .transactionStatus(transactionStatus)
        .transactionStatusMessage(transactionStatusMessage)
        .transactionTime(transactionTime)
        .userId(userId)
        .build();
  }
}
