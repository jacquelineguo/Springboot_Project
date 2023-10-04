package com.neu.easypay.controller;

import com.neu.easypay.mapper.PaymentDetailsMapper;
import com.neu.easypay.mapper.UserAccountMapper;
import com.neu.easypay.model.PaymentDetail;
import com.neu.easypay.model.UserAccount;
import com.neu.easypay.service.RefundService;
import com.neu.easypay.service.impl.RefundServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;


public class RefundControllerTest {
    @Mock
    RefundController refundController;
    @Mock
    RefundService refundService;
    @Mock
    PaymentDetailsMapper paymentDetailsMapper;
    @Mock
    UserAccountMapper userAccountMapper;

    @BeforeEach
    void setUp() {
        paymentDetailsMapper = mock(PaymentDetailsMapper.class);
        PaymentDetail paymentDetail = PaymentDetail.builder()
                .paymentAmount(300d).transactionId("1222")
                .transactionType("payment")
                .transactionStatus("complete")
                .userId("123")
                .payerId("231")
                .build();
        when(paymentDetailsMapper.getPaymentDetailByTransactionId("1222")).thenReturn(
                paymentDetail);

        PaymentDetail paymentDetail2 = PaymentDetail.builder()
                .paymentAmount(300d).transactionId("1223")
                .transactionType("refund")
                .transactionStatus("complete")
                .userId("231")
                .payerId("123")
                .build();
        when(paymentDetailsMapper.getPaymentDetailByTransactionId("1223")).thenReturn(
                paymentDetail2);

        PaymentDetail paymentDetail3 = PaymentDetail.builder()
                .paymentAmount(300d).transactionId("1224")
                .transactionType("payment")
                .transactionStatus("pending")
                .userId("123")
                .payerId("231")
                .build();
        when(paymentDetailsMapper.getPaymentDetailByTransactionId("1224")).thenReturn(
                paymentDetail3);

        PaymentDetail paymentDetail4 = PaymentDetail.builder()
                .paymentAmount(300d).transactionId("1225")
                .transactionType("payment")
                .transactionStatus("complete")
                .userId("444")
                .payerId("231")
                .build();
        when(paymentDetailsMapper.getPaymentDetailByTransactionId("1225")).thenReturn(
                paymentDetail4);

        PaymentDetail paymentDetail5 = PaymentDetail.builder()
                .paymentAmount(300d).transactionId("1226")
                .transactionType("refund")
                .transactionStatus("pending")
                .userId("231")
                .payerId("123")
                .build();
        when(paymentDetailsMapper.getPaymentDetailByTransactionId("1226")).thenReturn(
                paymentDetail5);

        PaymentDetail paymentDetail6 = PaymentDetail.builder()
                .paymentAmount(10000d).transactionId("1227")
                .transactionType("refund")
                .transactionStatus("complete")
                .userId("231")
                .payerId("123")
                .build();
        when(paymentDetailsMapper.getPaymentDetailByTransactionId("1227")).thenReturn(
                paymentDetail6);

        when(paymentDetailsMapper.deleteRefund(isA(String.class))).thenReturn(true);
        when(paymentDetailsMapper.changeStatus(isA(String.class), isA(String.class))).thenReturn(true);

        userAccountMapper = mock(UserAccountMapper.class);
        UserAccount payer = UserAccount.builder()
                .uuid("231")
                .balance(5000d)
                .build();
        userAccountMapper = mock(UserAccountMapper.class);
        UserAccount user = UserAccount.builder()
                .uuid("123")
                .balance(5000d)
                .build();

        when(userAccountMapper.getByUserUUID("123")).thenReturn(user);
        when(userAccountMapper.getByUserUUID("231")).thenReturn(payer);
        when(userAccountMapper.update(isA(String.class), isA(Double.class))).thenReturn(true);
        refundService = new RefundServiceImpl(paymentDetailsMapper, userAccountMapper);
        refundController = new RefundController(refundService);
    }

    @Test
    void refundFail() {
        ResponseEntity res1 = refundController.refund("1223", 300d);
        assertEquals("must equals", "not a valid transactionId", res1.getBody().toString());
        ResponseEntity res2 = refundController.refund("1224", 300d);
        assertEquals("must equals", "the transaction is not completed", res2.getBody().toString());
        ResponseEntity res3 = refundController.refund("1222", 5000d);
        assertEquals("must equals", "the refund amount can't be greater than payment amount", res3.getBody().toString());
        ResponseEntity res4 = refundController.refund("1225", 300d);
        assertEquals("must equals", "invalid transaction with wrong userId", res4.getBody().toString());

    }

    @Test
    void refundSuccess() {
        ResponseEntity res = refundController.refund("1222", 300d);
        assertEquals("must equals", "refund successfully", res.getBody().toString());
        verify(userAccountMapper, times(2)).update(isA(String.class), isA(Double.class));
    }

    @Test
    void cancelRefundFail() {
        ResponseEntity res1 = refundController.cancelRefund("1222", "1222");
        assertEquals("must equals", "not a valid refundId", res1.getBody().toString());
        ResponseEntity res2 = refundController.cancelRefund("1226", "1222");
        assertEquals("must equals", "the refund is not completed", res2.getBody().toString());
        ResponseEntity res3 = refundController.cancelRefund("1227", "1222");
        assertEquals("must equals", "you don't have enough money", res3.getBody().toString());
    }

    @Test
    void cancelRefundSuccess() {
        ResponseEntity res = refundController.cancelRefund("1223", "1222");
        assertEquals("must equals", "cancel refund successfully", res.getBody().toString());
        verify(paymentDetailsMapper, times(1)).deleteRefund(isA(String.class));
        verify(paymentDetailsMapper, times(1)).changeStatus(isA(String.class), isA(String.class));
        verify(userAccountMapper, times(2)).update(isA(String.class), isA(Double.class));
    }
}