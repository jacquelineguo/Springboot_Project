package com.neu.easypay.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninOutput {
    private final String username;
    private final String token;
}
