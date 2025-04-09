package com.example.springtemplate.billing.exception;

public class InvalidNameException extends RuntimeException
{
    public InvalidNameException(String message)
    {
        super(message);
    }
}
