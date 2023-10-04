package com.neu.easypay.controller;

import com.neu.easypay.model.BalanceResponse;
import com.neu.easypay.model.ExceptionOutput;
import com.neu.easypay.model.UserAccount;
import com.neu.easypay.service.ITransactionService;
import com.neu.easypay.service.impl.UserAccountService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

@Data
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final @NonNull UserAccountService usersAccountService;

    @Resource
    ITransactionService transactionService;

    @GetMapping(value = "/transaction")
    public ResponseEntity<?> getTransactionHistory(
            @RequestParam String userId,
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String transactionStatus
    ) {
        final UserAccount userAccount = usersAccountService.queryByUuid(userId);
        if (userAccount == null) {
            final ExceptionOutput exceptionOutput = ExceptionOutput.builder()
                    .message("User does not exist!")
                    .build();
            return new ResponseEntity<>(exceptionOutput, HttpStatus.NOT_FOUND);
        }
        if (transactionId != null && transactionStatus != null) {
            if (transactionService.getByTransactionIDandStatus(transactionId, transactionStatus).isEmpty()) {
                final ExceptionOutput exceptionOutput = ExceptionOutput.builder()
                        .message("No transaction found of such status!")
                        .build();
                return new ResponseEntity<>(exceptionOutput, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(transactionService.getByTransactionIDandStatus(transactionId, transactionStatus), HttpStatus.OK);
            }
        }
        if (transactionId != null) {
            if (transactionService.getByTransactionID(userId, transactionId) == null) {
                final ExceptionOutput exceptionOutput = ExceptionOutput.builder()
                        .message("No transaction found by given transaction ID!")
                        .build();
                return new ResponseEntity<>(exceptionOutput, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(transactionService.getByTransactionID(userId, transactionId), HttpStatus.OK);
            }
        }
        if (startDate != null && endDate != null) {
            DateTimeFormatter df =
                new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();
            LocalDate start = LocalDate.parse(startDate, df);
            LocalDate end = LocalDate.parse(endDate, df);

            System.out.println(start);
            LocalDateTime start1 = start.atStartOfDay();
            LocalDateTime end1 = end.atTime(23, 59);
            if (transactionService.getByDate(userId, start1, end1).isEmpty()) {
                final ExceptionOutput exceptionOutput = ExceptionOutput.builder()
                        .message("No transaction found during given date!")
                        .build();
                return new ResponseEntity<>(exceptionOutput, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(transactionService.getByDate(userId, start1, end1), HttpStatus.OK);
            }
        }
        if (transactionStatus != null) {
            if (transactionService.getByStatus(userId, transactionStatus).isEmpty()) {
                final ExceptionOutput exceptionOutput = ExceptionOutput.builder()
                        .message("No transaction found of given status and userId!")
                        .build();
                return new ResponseEntity<>(exceptionOutput, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(transactionService.getByStatus(userId, transactionStatus), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(transactionService.getAllTransactions(userId), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<?> getAccountBalance(
            @RequestParam String userId
    ) {
        final UserAccount userAccount = usersAccountService.queryByUuid(userId);
        if (userAccount == null) {
            final ExceptionOutput exceptionOutput = ExceptionOutput.builder()
                    .message("User does not exist!")
                    .build();
            return new ResponseEntity<>(exceptionOutput, HttpStatus.NOT_FOUND);
        } else {
            final String name = usersAccountService.queryByUuid(userId).getName();
            final Double balance = usersAccountService.queryByUuid(userId).getBalance();
            final BalanceResponse response = BalanceResponse.builder()
                    .name(name)
                    .balance(balance)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}

 