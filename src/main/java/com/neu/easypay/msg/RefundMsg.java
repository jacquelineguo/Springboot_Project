package com.neu.easypay.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class RefundMsg {
    private String res;
    private HttpStatus status;
}
