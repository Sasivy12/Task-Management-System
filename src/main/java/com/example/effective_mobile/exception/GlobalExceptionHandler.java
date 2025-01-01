package com.example.effective_mobile.exception;

import com.example.effective_mobile.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiResponse> handleTaskNotFound(TaskNotFoundException ex)
    {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleUserAlreadyExists(UserAlreadyExistsException ex)
    {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFound(UserNotFoundException ex)
    {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationFailed(AuthenticationFailedException ex)
    {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCommentNotFound(CommentNotFoundException ex)
    {
        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
