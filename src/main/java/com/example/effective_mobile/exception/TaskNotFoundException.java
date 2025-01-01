package com.example.effective_mobile.exception;

public class TaskNotFoundException extends RuntimeException
{
    public TaskNotFoundException(String message)
    {
        super(message);
    }
}
