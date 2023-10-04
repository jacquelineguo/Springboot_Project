package com.neu.easypay.model;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDetail {
  private final String transactionId;
  private final Date transactionTime;
  private final String transactionType; //refund or payment
  private final String transactionStatus; //complete, failure, or pending
  private final String transactionStatusMessage;
  private final String payerId;
  private final String payerName;
  private final String payerZipcode;
  private final String cardNumber;
  private final String cardType;
  private final Date cardExpDate;
  private final String cardCvv;
  private final Double paymentAmount;
  private final Double refundAmount;
  private final String userId;
}
