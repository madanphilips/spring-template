package com.example.springtemplate.billing.exception;

public class InvalidAmountException extends RuntimeException
{
    public InvalidAmountException(String message)
    {
        super(message);
    }
}
