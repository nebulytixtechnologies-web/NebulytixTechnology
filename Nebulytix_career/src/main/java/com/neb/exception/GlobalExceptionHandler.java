package com.neb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.neb.dto.CareerApplicationResponseDto;

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

    /**
     * Handles InvalidFileFormatException when an uploaded file has an unsupported format.
     */
    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<CareerApplicationResponseDto> handleInvalidFile(InvalidFileFormatException ex) {
        CareerApplicationResponseDto resp = new CareerApplicationResponseDto(null, "error", ex.getMessage());
        return ResponseEntity.badRequest().body(resp);
    }

    /**
     * Handles FileStorageException when file storage fails (e.g., writing to disk).
     */
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<CareerApplicationResponseDto> handleFileStorage(FileStorageException ex) {
        CareerApplicationResponseDto resp = new CareerApplicationResponseDto(null, "error", "File upload failed");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }

    /**
     * Handles PayslipGenerationException during payslip creation.
     */
    @ExceptionHandler(PayslipGenerationException.class)
    public ResponseEntity<CareerApplicationResponseDto> handlePayslipGenerationException(PayslipGenerationException ex) {
        CareerApplicationResponseDto resp = new CareerApplicationResponseDto(null, "error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }

    /**
     * Handles CustomeException (e.g., No schedule found for employee).
     */
    @ExceptionHandler(CustomeException.class)
    public ResponseEntity<CareerApplicationResponseDto> handleCustomeException(CustomeException ex) {
        CareerApplicationResponseDto resp = new CareerApplicationResponseDto(null, "error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
    }

    /**
     * Handles all other unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CareerApplicationResponseDto> handleGeneral(Exception ex) {
        CareerApplicationResponseDto resp = new CareerApplicationResponseDto(null, "error", "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}
