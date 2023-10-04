package com.neu.easypay.service.impl;

import com.neu.easypay.model.CardInfo;
import com.neu.easypay.model.CardValidationResponse;
import com.neu.easypay.model.PaymentProcessingResult;
import javax.annotation.Resource;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Builder
@Service
public class DebitCardPaymentProcessor extends PaymentProcessorWithDbPersist {
  @Resource
  CardValidationService cardValidationService;

  @Override
  public PaymentProcessingResult processPayment(String payerId, CardInfo cardInfo, String userId, Double amount) {
    // Validate card
    CardValidationResponse validationResponse = cardValidationService
        .checkDebitCardSufficientFund(cardInfo.getCardNumber(), amount);

    String transactionStatus;
    String transactionStatusMessage;
    if (validationResponse.isSuccess()) {
      transactionStatus = COMPLETE;
      transactionStatusMessage =
          "Your " + getPaymentType().toLowerCase() + " payment has been processed successfully.";
    } else {
      transactionStatus = FAILED;
      transactionStatusMessage = validationResponse.getMsg();
    }

    // Persist details
    return persistPaymentDetail(transactionStatus, transactionStatusMessage, payerId, cardInfo, amount, userId);
  }

  public String getPaymentType() {
    return "DEBIT";
  }
}
