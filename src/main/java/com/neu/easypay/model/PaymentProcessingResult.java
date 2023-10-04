package com.neu.easypay.model;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentProcessingResult {
  private final String transactionId;
  private final Date transactionTime;
  private final String transactionStatus;
  private final String transactionStatusMessage;
  private final String userId;
}
