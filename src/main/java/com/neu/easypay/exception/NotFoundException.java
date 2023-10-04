package com.neu.easypay.exception;

public class NotFoundException extends Exception {
    public NotFoundException() {
        super("Cannot find record.");
    }
}
