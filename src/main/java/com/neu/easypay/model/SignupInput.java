package com.neu.easypay.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupInput {
    private String username;
    private String email;
    private String password;
    private String name;
}
