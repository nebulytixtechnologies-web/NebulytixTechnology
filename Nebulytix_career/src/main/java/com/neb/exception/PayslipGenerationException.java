package com.neb.exception;



/**
 * ---------------------------------------------------------------
 * File Name   : PayslipGenerationException.java
 * Package     : com.neb.exception
 * ---------------------------------------------------------------
 * Purpose :
 *   Custom exception for handling errors during payslip generation.
 *
 * Description :
 *   - Thrown when the system fails to generate a payslip for an employee.
 *   - Helps identify and handle issues related to automatic or manual
 *     payslip generation (e.g., missing data, IO errors, etc.).
 *
 * Constructor :
 *   PayslipGenerationException(String message)
 *       → Accepts a custom error message describing the issue.
 * ---------------------------------------------------------------
 */
@SuppressWarnings("serial")
public class PayslipGenerationException extends RuntimeException {

    public PayslipGenerationException(String message) {
        super(message);
    }

    public PayslipGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}

