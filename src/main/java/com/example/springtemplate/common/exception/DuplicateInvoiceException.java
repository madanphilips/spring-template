package com.example.springtemplate.common.exception;

public class DuplicateInvoiceException extends RuntimeException {

    public DuplicateInvoiceException(String message) {
        super(message);
    }
}
