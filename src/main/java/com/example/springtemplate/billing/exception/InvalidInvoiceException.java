package com.example.springtemplate.billing.exception;

public class InvalidInvoiceException extends RuntimeException {
    public InvalidInvoiceException(String message) {
        super(message);
    }
}