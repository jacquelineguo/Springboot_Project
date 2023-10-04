package com.neu.easypay.mapper;

import com.neu.easypay.model.TransactionInfo;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TransactionMapper {
    @Select
        ("SELECT transactionId, userId, transactionTime, paymentAmount, cardType, transactionStatus, payerId, transactionType FROM PaymentDetails WHERE userId = #{userId}")
    List<TransactionInfo> getAllTransactions(@Param("userId") String userId);

    @Select
        ("SELECT transactionId, userId, transactionTime, paymentAmount, cardType, transactionStatus, payerId, transactionType FROM PaymentDetails WHERE userId = #{userId} AND transactionId = #{transactionId}")
    TransactionInfo getTransactionByID(@Param("userId") String userId,@Param("transactionId") String transactionId);

    @Select
            ("SELECT transactionId, userId, transactionTime, paymentAmount, cardType, transactionStatus, payerId, transactionType FROM PaymentDetails WHERE userId = #{userId} AND transactionTime BETWEEN #{startDate} AND #{endDate}")
    List<TransactionInfo> getByDate(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Select
            ("SELECT transactionId, userId, transactionTime, paymentAmount, cardType, transactionStatus, payerId, transactionType FROM PaymentDetails WHERE transactionStatus = #{transactionStatus} AND userId = #{userId}")
    List<TransactionInfo> getByStatus(@Param("userId") String userId, @Param("transactionStatus") String transactionStatus);

    @Select
            ("SELECT transactionId, userId, transactionTime, paymentAmount, cardType, transactionStatus, payerId, transactionType FROM PaymentDetails WHERE transactionId = #{transactionId} AND transactionStatus = #{transactionStatus}")
    List<TransactionInfo> getByTransactionIDandStatus(@Param("transactionId") String transactionId, @Param("transactionStatus") String transactionStatus);

}
