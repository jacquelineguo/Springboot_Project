package com.neu.easypay.exception;

public class DuplicateException extends Exception {
    public DuplicateException() {
        super("Cannot create duplicate record.");
    }
}
