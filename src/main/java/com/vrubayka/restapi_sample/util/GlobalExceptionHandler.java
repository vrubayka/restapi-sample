package com.vrubayka.restapi_sample.util;

import java.sql.SQLException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.PersistenceException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleDuplicateEmail(ConstraintViolationException ex) {
        // Log the exact exception with details
        logger.error("This email has already been registered to a different account", ex);
        return new ResponseEntity<>("This email has already been registered to a different account: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<String> handleDatabaseError(PersistenceException ex) {
        // Log the exact exception with details
        logger.error("Database error: ", ex);
        return new ResponseEntity<>("Database error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException ex) {
        // Log the exact exception with details
        logger.error("SQL error: ", ex);
        if ("23505".equals(ex.getSQLState())) { // Duplicate key error
            return new ResponseEntity<>("This email has already been registered to a different account.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SQL error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        // Log the exact exception with details
        logger.error("An unexpected error occurred: ", ex);
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
