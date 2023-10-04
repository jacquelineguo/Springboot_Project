package com.neu.easypay.controller;

import com.neu.easypay.msg.RefundMsg;
import com.neu.easypay.service.RefundService;
import com.neu.easypay.service.impl.RefundServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
public class RefundController {
    @Resource
    RefundService refundService;

    @PostMapping("/refund")
    public ResponseEntity<?> refund(@RequestParam String transactionId, @RequestParam(required = false) Double paymentAmount) {
        RefundMsg res = refundService.refund(transactionId, paymentAmount);
        return new ResponseEntity<>(res.getRes(), res.getStatus());
    }

    @DeleteMapping("/refund/cancel")
    public ResponseEntity<?> cancelRefund(@RequestParam String refundId, @RequestParam String transactionId) {
        RefundMsg res = refundService.cancelRefund(refundId, transactionId);
        return new ResponseEntity<>(res.getRes(), res.getStatus());
    }
}