package com.neu.easypay.controller;

import com.neu.easypay.mapper.PaymentDetailsMapper;
import com.neu.easypay.model.CardInfo;
import com.neu.easypay.model.ExceptionOutput;
import com.neu.easypay.model.GetPaymentDetailOutput;
import com.neu.easypay.model.PaymentDetail;
import com.neu.easypay.model.PaymentInput;
import com.neu.easypay.model.PaymentProcessingResult;
import com.neu.easypay.model.PaymentType;
import com.neu.easypay.service.PaymentProcessor;
import com.neu.easypay.service.impl.PaymentProcessorFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
public class PaymentController {
//  @Resource
//  PaymentServiceI paymentService;

  @Resource
  PaymentProcessorFactory paymentProcessorFactory;

  @Resource
  PaymentDetailsMapper mapper;

  @PostMapping("/payment")
  public ResponseEntity<?> makePayment(
      @RequestBody PaymentInput input
  ) throws ParseException {

    // Validate input
    ResponseEntity<ExceptionOutput> potentialException = validateInput(input);
    if (!Objects.isNull(potentialException)) {
      return potentialException;
    }

    // Get payment processor.
    PaymentType paymentType;
    try {
      paymentType = PaymentType.valueOf(input.getCardType());
    } catch (IllegalArgumentException exception) {
      return respondWithError(
          input.getCardType() + " is not supported");
    }

    PaymentProcessor paymentProcessor = paymentProcessorFactory.getPaymentProcessor(paymentType);
    SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy", Locale.ENGLISH);

    Date expDate = formatter.parse(input.getCardExpDate());

    CardInfo cardInfo = CardInfo.builder()
        .cardNumber(input.getCardNumber())
        .cardHolderName(input.getPayerName())
        .cardHolderZipcode(input.getPayerZipcode())
        .cardExpDate(expDate)
        .cardCvv(input.getCardCvv())
        .build();

    // Process payment
    PaymentProcessingResult result = paymentProcessor.processPayment(
        input.getPayerId(),
        cardInfo,
        input.getUserId(),
        input.getPaymentAmount()
    );

    // Return result
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  private ResponseEntity<ExceptionOutput> respondWithError(String message) {
    ExceptionOutput exceptionOutput = ExceptionOutput.builder()
        .message(message)
        .build();
    return new ResponseEntity<>(exceptionOutput, HttpStatus.BAD_REQUEST);
  }

  @SneakyThrows
  private ResponseEntity<ExceptionOutput> validateInput(PaymentInput paymentInput) {
    if (String.valueOf(paymentInput.getCardNumber()).length() != 16) {
      return respondWithError("Invalid card number: " + paymentInput.getCardNumber() + ". "
          + "Valid card number must be 16 digit. Please update card number and retry.");
    }

    if (paymentInput.getPaymentAmount() <= 0) {
      return respondWithError("Invalid payment amount.");
    }

    SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy", Locale.ENGLISH);
    Date expDate = formatter.parse(paymentInput.getCardExpDate());
    if (expDate.before(new Date())) {
      return respondWithError("Your credit card has expired.");
    }

    return null;
  }

  @GetMapping("/paymentDetail")
  public ResponseEntity<?> getPaymentDetail(
      @RequestParam String transactionId
  ) {
    // Retrieve persisted payment detail
    PaymentDetail paymentDetail = mapper.getPaymentDetailByTransactionId(transactionId);
    if (paymentDetail == null) {
      return respondWithError("Invalid payment id" + transactionId);
    }

    // Return relevant data as response
    String originalCardNumber = paymentDetail.getCardNumber();
    String maskedCardNumber = "Card number ending with " + originalCardNumber
        .substring(originalCardNumber.length() - 4, originalCardNumber.length());

    GetPaymentDetailOutput output = GetPaymentDetailOutput.builder()
        .transactionTime(paymentDetail.getTransactionTime())
        .transactionStatus(paymentDetail.getTransactionStatus())
        .transactionStatusMessage(paymentDetail.getTransactionStatusMessage())
        .paymentAmount(paymentDetail.getPaymentAmount())
        .paymentType(paymentDetail.getCardType())
        .userId(paymentDetail.getUserId())
        .payerId(paymentDetail.getPayerId())
        .payerName(paymentDetail.getPayerName())
        .cardNumber(maskedCardNumber)
        .build();

    return new ResponseEntity<>(output, HttpStatus.OK);
  }

  /* payment validation (regular validation and fraud check for credit card payment) */
//  @PostMapping("/payment/check")
//  public PaymentValidationResult checkPayment(@RequestBody CreditCard cardToCheck) throws ParseException {
//    cardToCheck.setExpireDate(cardToCheck.getExpireDate() + "-01");
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//    Date date = formatter.parse(cardToCheck.getExpireDate());
//    if (!(isNumeric(cardToCheck.getCardNumber()) && cardToCheck.getCardNumber().length() == 16
//        && date.after(new Date()))) {
//      return new PaymentValidationResult("Invalid credit card!", 400);
//    }
//
//    return paymentService.check(cardToCheck)
//        ? new PaymentValidationResult("Success", 200)
//        : new PaymentValidationResult("Fraud payment detected!", 400);
//  }
//
//  private static boolean isNumeric(String str) {
//    for (char c : str.toCharArray()) {
//      if (!Character.isDigit(c)) {
//        return false;
//      }
//    }
//    return true;
//  }
}
