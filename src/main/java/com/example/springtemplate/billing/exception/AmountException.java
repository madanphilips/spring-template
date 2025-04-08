package com.example.springtemplate.billing.exception;

public class AmountException extends RuntimeException
{
    public AmountException(String message)
    {
        super(message);
    }
}
