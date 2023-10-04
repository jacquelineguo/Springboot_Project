package com.neu.easypay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentValidationResult {
  private String result;
  private Integer code;
}
