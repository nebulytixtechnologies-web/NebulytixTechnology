package com.neb.dto;

import lombok.Data;

/**
 * ---------------------------------------------------------------
 * File Name   : GeneratePayslipRequest.java
 * Package     : com.neb.dto
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used to capture the necessary input data
 *   for generating an employee’s payslip for a specific month.
 *
 * Description :
 *   - It serves as a Data Transfer Object (DTO) that carries
 *     the employee ID and the target month-year from the frontend
 *     to the backend service.
 *   - The backend then uses this information to calculate
 *     salary, deductions, and generate a payslip record.
 *
 * Fields :
 *   employeeId  → Unique identifier of the employee for whom
 *                  the payslip is to be generated
 *   monthYear   → Specifies the month and year of the payslip
 *                  (e.g., "August 2025")
 *
 * Result :
 *   This DTO helps in triggering the payslip generation process
 *   by providing the backend with minimal and precise data.
 * ---------------------------------------------------------------
 */


@Data
public class GeneratePayslipRequest {
    private Long employeeId;
    private String monthYear; // e.g., "August 2025"
}
