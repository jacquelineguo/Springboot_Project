package com.neu.easypay.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class ExceptionOutput {
    private final String message;
}
