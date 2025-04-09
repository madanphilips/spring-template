package com.example.springtemplate.billing.exception;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.springtemplate.common.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidFormat(HttpMessageNotReadableException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Invalid input format");
        error.put("details", ex.getMessage());
        error.put("status", 400);
        error.put("timestamp", Instant.now().toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDuplicateKey(DataIntegrityViolationException ex) {
        @SuppressWarnings("null")
        String rootMsg = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "";
        String userMsg = "Data already exists";
        Pattern pattern = Pattern.compile("Duplicate entry '(.+?)' for key '(.+?)'");
        Matcher matcher = pattern.matcher(rootMsg);
    
        if (matcher.find()) {
            String duplicateValue = matcher.group(1);
            userMsg = String.format("Value '%s' already exists", duplicateValue);
        }
    
        Map<String, Object> error = new HashMap<>();
        error.put("message", userMsg);
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("timestamp", Instant.now().toString());
    
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("message", "Internal Server Error");
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("details", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
