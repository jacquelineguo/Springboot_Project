package com.neu.easypay.service.impl;

import com.neu.easypay.mapper.PaymentDetailsMapper;
import com.neu.easypay.mapper.UserAccountMapper;
import com.neu.easypay.model.PaymentDetail;
import com.neu.easypay.msg.RefundMsg;
import com.neu.easypay.service.RefundService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RefundServiceImpl implements RefundService {
    @Resource
    PaymentDetailsMapper paymentDetailsMapper;

    @Resource
    UserAccountMapper userAccountMapper;

    @Override
    public RefundMsg refund(String transactionID, Double amount) {
        PaymentDetail paymentDetail = paymentDetailsMapper.getPaymentDetailByTransactionId(transactionID);

        if (paymentDetail == null || !paymentDetail.getTransactionType().equals("payment")) {
            return new RefundMsg("not a valid transactionId", HttpStatus.FORBIDDEN);
        }

        if (!paymentDetail.getTransactionStatus().equals("complete")) {
            if (paymentDetail.getTransactionStatus().equals("refund")) {
                return new RefundMsg("can't refund for multiple times", HttpStatus.FORBIDDEN);
            }
            return new RefundMsg("the transaction is not completed", HttpStatus.FORBIDDEN);
        }

        amount = (amount == null ? paymentDetail.getPaymentAmount() : amount);
        if (amount > paymentDetail.getPaymentAmount()) {
            return new RefundMsg("the refund amount can't be greater than payment amount", HttpStatus.FORBIDDEN);
        }
        //TO DO：change balance. Need some change in userAccountService
        String userId = paymentDetail.getUserId();
        String receiverId = paymentDetail.getPayerId();

        if (userAccountMapper.getByUserUUID(userId) == null || userAccountMapper.getByUserUUID(receiverId) == null) {
            return new RefundMsg("invalid transaction with wrong userId", HttpStatus.FORBIDDEN);
        }

        if (userAccountMapper.getByUserUUID(receiverId).getBalance() < amount) {
            return new RefundMsg("abnormal status of the seller, please contact seller", HttpStatus.FORBIDDEN);
        }

        userAccountMapper.update(userId, userAccountMapper.queryBalance(userId) + amount);
        userAccountMapper.update(receiverId, userAccountMapper.queryBalance(receiverId) - amount);


        String refundId = UUID.randomUUID().toString();
        PaymentDetail refundDetail = PaymentDetail.builder()
                .transactionId(refundId)
                .transactionTime(new Date())
                .transactionType("refund")
                .transactionStatus(paymentDetail.getTransactionStatus())
                .transactionStatusMessage(paymentDetail.getTransactionStatusMessage())
                .payerId(paymentDetail.getPayerId())
                .payerName(paymentDetail.getPayerName())
                .payerZipcode(paymentDetail.getPayerZipcode())
                .cardNumber(paymentDetail.getCardNumber())
                .cardType(paymentDetail.getCardType())
                .cardExpDate(paymentDetail.getCardExpDate())
                .cardCvv(paymentDetail.getCardCvv())
                .paymentAmount(paymentDetail.getPaymentAmount())
                .refundAmount(amount)
                .userId(userId)
                .build();
        paymentDetailsMapper.insertRefund(refundDetail);
        paymentDetailsMapper.changeStatus(transactionID, "refund");
        return new RefundMsg("refund successfully", HttpStatus.OK);
    }

    @Override
    public RefundMsg cancelRefund(String refundId, String transactionId) {
        PaymentDetail paymentDetail = paymentDetailsMapper.getPaymentDetailByTransactionId(refundId);
        if (paymentDetail == null || !paymentDetail.getTransactionType().equals("refund")) {
            return new RefundMsg("not a valid refundId", HttpStatus.FORBIDDEN);
        }
        if (!paymentDetail.getTransactionStatus().equals("complete")) {
            return new RefundMsg("the refund is not completed", HttpStatus.FORBIDDEN);
        }
        if (userAccountMapper.getByUserUUID(paymentDetail.getUserId()).getBalance() < paymentDetail.getPaymentAmount()) {
            return new RefundMsg("you don't have enough money", HttpStatus.FORBIDDEN);
        }

        paymentDetailsMapper.deleteRefund(refundId);
        paymentDetailsMapper.changeStatus(transactionId, "complete");
        userAccountMapper.update(paymentDetail.getUserId(), userAccountMapper.queryBalance(paymentDetail.getUserId()) + paymentDetail.getPaymentAmount());
        userAccountMapper.update(paymentDetail.getPayerId(), userAccountMapper.queryBalance(paymentDetail.getPayerId()) - paymentDetail.getPaymentAmount());

        return new RefundMsg("cancel refund successfully", HttpStatus.OK);
    }
}