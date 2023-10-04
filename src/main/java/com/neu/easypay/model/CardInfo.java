package com.neu.easypay.model;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CardInfo {
  private final String cardHolderName;
  private final String cardHolderZipcode;
  private final String cardNumber;
  private final Date cardExpDate;
  private final String cardCvv;
}
