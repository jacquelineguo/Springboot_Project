package com.neu.easypay.model;

import lombok.Data;

@Data
public class CardValidationResponse {
    private boolean success;
    private String msg;
}
