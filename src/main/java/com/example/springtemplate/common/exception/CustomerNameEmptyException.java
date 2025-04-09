package com.example.springtemplate.common.exception;

public class CustomerNameEmptyException extends RuntimeException {

    public CustomerNameEmptyException(String message) {
        super(message);
    }
}
