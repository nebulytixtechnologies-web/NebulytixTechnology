package com.neb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * --------------------------------------------------------------
 * Global exception handler for the application.
 *
 * Description:
 *  - Catches and handles all custom and general exceptions globally.
 *  - Returns a consistent response structure using CareerApplicationResponseDto.
 *  - Includes handlers for file upload, payslip generation, and schedule errors.
 * --------------------------------------------------------------
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    
}
