package com.neu.easypay.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInput {
  String payerId;
  String payerName;
  String payerZipcode;

  String cardType;
  String cardNumber;
  String cardExpDate;
  String cardCvv;

  Double paymentAmount;

  String userId;
}
