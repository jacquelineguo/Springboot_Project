package com.neu.easypay.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.neu.easypay.mapper.PaymentDetailsMapper;
import com.neu.easypay.model.GetPaymentDetailOutput;
import com.neu.easypay.model.PaymentDetail;
import com.neu.easypay.model.PaymentInput;
import com.neu.easypay.model.PaymentProcessingResult;
import com.neu.easypay.model.PaymentType;
import com.neu.easypay.service.impl.DebitCardPaymentProcessor;
import com.neu.easypay.service.impl.PaymentProcessorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class PaymentControllerTest {

  private PaymentController controller;

  @Mock
  PaymentDetailsMapper mapper;

  @Mock
  PaymentProcessorFactory paymentProcessorFactory;

  @Mock
  DebitCardPaymentProcessor debitCardPaymentProcessor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    controller = new PaymentController();
    controller.setPaymentProcessorFactory(paymentProcessorFactory);
    controller.setMapper(mapper);
    when(paymentProcessorFactory.getPaymentProcessor(PaymentType.DEBIT))
        .thenReturn(debitCardPaymentProcessor);
  }

  @Test
  void makePayment() throws Exception {
    PaymentProcessingResult result = PaymentProcessingResult.builder().build();

    when(debitCardPaymentProcessor.processPayment(any(), any(), any(), any())).thenReturn(result);

    PaymentInput input = PaymentInput.builder()
        .payerId("payer1")
        .payerName("payer name")
        .payerZipcode("98101")
        .cardType("DEBIT")
        .cardNumber("1234123412341238")
        .cardExpDate("11-2029")
        .cardCvv("123")
        .paymentAmount(12.5)
        .userId("user1")
        .build();

    ResponseEntity responseEntity = controller.makePayment(input);

    assertEquals(result.toString(), responseEntity.getBody().toString());
  }

  @Test
  void makePayment_cardExpired() throws Exception {
    PaymentProcessingResult result = PaymentProcessingResult.builder().build();

    when(debitCardPaymentProcessor.processPayment(any(), any(), any(), any())).thenReturn(result);

    PaymentInput input = PaymentInput.builder()
        .payerId("payer1")
        .payerName("payer name")
        .payerZipcode("98101")
        .cardType("DEBIT")
        .cardNumber("1234123412341238")
        .cardExpDate("11-2020")
        .cardCvv("123")
        .paymentAmount(12.5)
        .userId("user1")
        .build();

    ResponseEntity responseEntity = controller.makePayment(input);

    assertEquals("ExceptionOutput(message=Your credit card has expired.)",
        responseEntity.getBody().toString());
  }

  @Test
  void makePayment_invalidCard() throws Exception {
    PaymentProcessingResult result = PaymentProcessingResult.builder().build();

    when(debitCardPaymentProcessor.processPayment(any(), any(), any(), any())).thenReturn(result);

    PaymentInput input = PaymentInput.builder()
        .payerId("payer1")
        .payerName("payer name")
        .payerZipcode("98101")
        .cardType("DEBIT")
        .cardNumber("123412341234138")
        .cardExpDate("11-2020")
        .cardCvv("123")
        .paymentAmount(12.5)
        .userId("user1")
        .build();

    ResponseEntity responseEntity = controller.makePayment(input);

    assertEquals(
        "ExceptionOutput(message=Invalid card number: 123412341234138. Valid card number must be 16 digit. Please update card number and retry.)",
        responseEntity.getBody().toString());
  }

  @Test
  void getPaymentDetail() {
    PaymentDetail paymentDetail = PaymentDetail.builder()
        .cardNumber("12345")
        .build();
    when(mapper.getPaymentDetailByTransactionId("123")).thenReturn(paymentDetail);

    ResponseEntity output = controller.getPaymentDetail("123");

    assertEquals(
        "GetPaymentDetailOutput(transactionTime=null, transactionStatus=null, transactionStatusMessage=null, payerId=null, payerName=null, cardNumber=Card number ending with 2345, paymentType=null, paymentAmount=null, userId=null)",
        output.getBody().toString());
  }
}