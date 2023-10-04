package com.neu.easypay.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.neu.easypay.mapper.PaymentDetailsMapper;
import com.neu.easypay.mapper.UserAccountMapper;
import com.neu.easypay.model.CardInfo;
import com.neu.easypay.model.PaymentProcessingResult;
import com.neu.easypay.model.UserAccount;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreditCardPaymentProcessorTest {

  private CreditCardPaymentProcessor processor;

  @Mock
  private UserAccountMapper userAccountMapper;

  @Mock
  private PaymentDetailsMapper paymentDetailsMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    CardValidationService validationService = new CardValidationService();

    processor = CreditCardPaymentProcessor.builder()
        .cardValidationService(validationService)
        .build();

    processor.setUserAccountMapper(userAccountMapper);
    processor.setPaymentDetailsMapper(paymentDetailsMapper);
  }

  @Test
  void processPayment_fail() {
    UserAccount userAccount = UserAccount.builder()
        .balance(20.0)
        .build();
    when(userAccountMapper.getByUserUUID("user1")).thenReturn(userAccount);

    CardInfo cardInfo = CardInfo.builder()
        .cardNumber("123456")
        .cardHolderName("Test Name")
        .cardCvv("123")
        .cardExpDate(new Date())
        .cardHolderZipcode("98101")
        .build();
    PaymentProcessingResult result = processor.processPayment("payer1", cardInfo, "user1", 10.0);

    assertEquals("failure", result.getTransactionStatus());
    assertEquals(
        "The card number 123456 is invalid because the last digit or check digit should have been 5",
        result.getTransactionStatusMessage());
    assertEquals("user1", result.getUserId());
  }

  @Test
  void processPayment_success() {
    UserAccount userAccount = UserAccount.builder()
        .balance(20.0)
        .build();
    when(userAccountMapper.getByUserUUID("user1")).thenReturn(userAccount);

    CardInfo cardInfo = CardInfo.builder()
        .cardNumber("12345678901237")
        .cardHolderName("Test Name")
        .cardCvv("123")
        .cardExpDate(new Date())
        .cardHolderZipcode("98101")
        .build();
    PaymentProcessingResult result = processor.processPayment("payer1", cardInfo, "user1", 10.0);

    assertEquals("complete", result.getTransactionStatus());
    assertEquals(
        "Your credit payment has been initiated.",
        result.getTransactionStatusMessage());
    assertEquals("user1", result.getUserId());
  }

  @Test
  void getPaymentType() {
    assertEquals("CREDIT", processor.getPaymentType());
  }
}