package com.neu.easypay.model;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPaymentDetailOutput {
  private final Date transactionTime;
  private final String transactionStatus;
  private final String transactionStatusMessage;
  private final String payerId;
  private final String payerName;
  private final String cardNumber;
  private final String paymentType;
  private final Double paymentAmount;
  private final String userId;
}
