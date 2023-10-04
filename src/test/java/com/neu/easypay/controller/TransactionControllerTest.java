package com.neu.easypay.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.neu.easypay.model.BalanceResponse;
import com.neu.easypay.model.ExceptionOutput;
import com.neu.easypay.model.TransactionInfo;
import com.neu.easypay.model.UserAccount;
import com.neu.easypay.service.ITransactionService;
import com.neu.easypay.service.impl.UserAccountService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


public class TransactionControllerTest {
  @Mock
  ITransactionService transactionService;

  @Mock
  UserAccountService usersAccountService;

  private TransactionController controller;

  private UserAccount mockUserAccount = UserAccount.builder()
      .build();

  private String userId = "TestUser";
  private String transactionId = "transactionId";
  private String startDate = "1970-01-01";
  private String endDate = "1980-01-01";
  private String status = "completed";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    controller = new TransactionController(usersAccountService);
    controller.setTransactionService(transactionService);
  }

  @Test
  void getTransactionHistoryByUserId() {
    List<TransactionInfo> expected = new ArrayList<>();
    expected.add(TransactionInfo.builder().build());
    when(transactionService.getAllTransactions(any())).thenReturn(expected);
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);
    transactionId = null;
    startDate = null;
    endDate = null;
    status = null;

    ResponseEntity response = controller.getTransactionHistory(userId, transactionId, startDate, endDate, status);

    assertEquals(expected.toString(), response.getBody().toString());
  }

  @Test
  void getTransactionHistoryByInvalidUserId() {
    List<TransactionInfo> mockTransactionInfo = new ArrayList<>();
    mockTransactionInfo.add(TransactionInfo.builder().build());
    ExceptionOutput expected = ExceptionOutput.builder()
        .message("User does not exist!")
        .build();
    when(transactionService.getAllTransactions(any())).thenReturn(mockTransactionInfo);

    ResponseEntity response = controller.getTransactionHistory(userId, transactionId, startDate, endDate, status);

    assertEquals(expected.toString(), response.getBody().toString());
  }

  @Test
  void getTransactionByInvalidTransactionIdAndStatus() {
    List<TransactionInfo> mockTransactionInfo = new ArrayList<>();
    ExceptionOutput expected = ExceptionOutput.builder()
        .message("No transaction found of such status!")
        .build();
    when(transactionService.getByTransactionIDandStatus(any(), any())).thenReturn(mockTransactionInfo);
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);

    ResponseEntity response = controller.getTransactionHistory(userId, transactionId, startDate, endDate, status);
    assertEquals(expected.toString(), response.getBody().toString());
  }

  @Test
  void getTransactionByTransactionIdAndStatus() {
    List<TransactionInfo> mockTransactionInfo = new ArrayList<>();
    mockTransactionInfo.add(TransactionInfo.builder().build());
    when(transactionService.getByTransactionIDandStatus(any(), any())).thenReturn(mockTransactionInfo);
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);

    ResponseEntity response = controller.getTransactionHistory(userId, transactionId, startDate, endDate, status);

    assertEquals(mockTransactionInfo.toString(), response.getBody().toString());
  }

  @Test
  void getByInvalidTransactionId() {
    ExceptionOutput exceptionOutput = ExceptionOutput.builder()
        .message("No transaction found by given transaction ID!")
        .build();
    when(transactionService.getByTransactionID(any(), any())).thenReturn(null);
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);

    ResponseEntity response = controller.getTransactionHistory(userId, transactionId, startDate,
        endDate, null);

    assertEquals(exceptionOutput.toString(), response.getBody().toString());
  }

  @Test
  void getByTransactionId() {
    TransactionInfo mock = TransactionInfo.builder().build();

    when(transactionService.getByTransactionID(any(), any())).thenReturn(mock);
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);

    ResponseEntity response = controller.getTransactionHistory(userId, transactionId, startDate,
        endDate, null);

    assertEquals(mock.toString(), response.getBody().toString());
  }

  @Test
  void getTransactionByDateNotFound() {
    List<TransactionInfo> mockTransactionInfo = new ArrayList<>();
    ExceptionOutput expected = ExceptionOutput.builder()
        .message("No transaction found during given date!")
        .build();
    when(transactionService.getByDate(any(), any(), any())).thenReturn(mockTransactionInfo);
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);

    ResponseEntity response = controller.getTransactionHistory(userId, null, startDate, endDate, null);
    assertEquals(expected.toString(), response.getBody().toString());
  }

  @Test
  void getTransactionByDate() {
    List<TransactionInfo> mockTransactionInfo = new ArrayList<>();
    mockTransactionInfo.add(TransactionInfo.builder().build());
    when(transactionService.getByDate(any(), any(), any())).thenReturn(mockTransactionInfo);
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);

    ResponseEntity response = controller.getTransactionHistory(userId, null, startDate, endDate, null);

    assertEquals(mockTransactionInfo.toString(), response.getBody().toString());
  }

  @Test
  void getTransactionByInvalidStatus() {
    List<TransactionInfo> mockTransactionInfo = new ArrayList<>();
    ExceptionOutput exceptionOutput = ExceptionOutput.builder()
        .message("No transaction found of given status and userId!")
        .build();
    when(transactionService.getByStatus(any(), any())).thenReturn(mockTransactionInfo);
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);

    ResponseEntity response = controller.getTransactionHistory(userId, null, null,
        null, status);

    assertEquals(exceptionOutput.toString(), response.getBody().toString());
  }

  @Test
  void getByStatus() {
    List<TransactionInfo> mockTransactionInfo = new ArrayList<>();
    mockTransactionInfo.add(TransactionInfo.builder().build());
    when(transactionService.getByStatus(any(), any())).thenReturn(mockTransactionInfo);
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);

    ResponseEntity response = controller.getTransactionHistory(userId, null, null,
        null, status);

    assertEquals(mockTransactionInfo.toString(), response.getBody().toString());
  }

  @Test
  void getBalanceByInvalidUserId() {
    ExceptionOutput exceptionOutput = ExceptionOutput.builder()
        .message("User does not exist!")
        .build();
    ResponseEntity response = controller.getAccountBalance(null);

    assertEquals(exceptionOutput.toString(), response.getBody().toString());
  }

  @Test
  void getBalance() {
    when(usersAccountService.queryByUuid(any())).thenReturn(mockUserAccount);
    BalanceResponse expectedOutput = BalanceResponse.builder().build();

    ResponseEntity response = controller.getAccountBalance(userId);

    assertEquals(expectedOutput.toString(), response.getBody().toString());
  }
}
