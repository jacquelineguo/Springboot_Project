package com.neu.easypay.mapper;

import com.neu.easypay.model.PaymentDetail;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PaymentDetailsMapper {
    @Select("select "
            + "transactionId, transactionTime, transactionStatus, transactionStatusMessage, transactionType,"
            + "payerId, payerName, payerZipcode, "
            + "cardNumber, cardType, cardExpDate, cardCvv, "
            + "paymentAmount, refundAmount, "
            + "userId "
            + "from PaymentDetails "
            + "where transactionId = #{transactionId}")
    PaymentDetail getPaymentDetailByTransactionId(String transactionId);

    @Insert("insert into PaymentDetails values (#{transactionId},#{transactionTime},#{transactionStatus},#{transactionStatusMessage},#{transactionType},"
            + "#{payerId},#{payerName},#{payerZipcode},"
            + "#{cardNumber},#{cardType},#{cardExpDate},#{cardCvv},"
            + "#{paymentAmount},#{refundAmount},"
            + "#{userId})")
    void insertPaymentDetail(PaymentDetail paymentDetail);

    @Insert("insert into PaymentDetails values (#{transactionId},#{transactionTime},#{transactionType},#{transactionStatus},#{transactionStatusMessage},"
            + "#{payerId},#{payerName},#{payerZipcode},"
            + "#{cardNumber},#{cardType},#{cardExpDate},#{cardCvv},"
            + "#{paymentAmount},#{refundAmount},"
            + "#{userId})")
    void insertRefund(PaymentDetail refundDetail);

    @Delete("delete from PaymentDetails where transactionId = #{refundId}")
    boolean deleteRefund(String refundId);

    @Update("update PaymentDetails set transactionStatus = #{transactionStatus} where transactionId = #{transactionId}")
    boolean changeStatus(String transactionId, String transactionStatus);
}
