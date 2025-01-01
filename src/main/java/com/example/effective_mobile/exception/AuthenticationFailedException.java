package com.example.effective_mobile.exception;

public class AuthenticationFailedException extends RuntimeException
{
    public AuthenticationFailedException(String message)
    {
        super(message);
    }
}
