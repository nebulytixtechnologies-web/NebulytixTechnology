package com.neb.dto;


import java.time.LocalDate;
import lombok.Data;

/**
 * ---------------------------------------------------------------
 * File Name   : EmployeeResponseDto.java
 * Package     : com.neb.dto
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used to transfer employee information from the
 *   backend to the frontend after successful operations such as
 *   login or profile retrieval.
 *
 * Description :
 *   - It acts as a Data Transfer Object (DTO) containing essential
 *     employee details required for display or authentication purposes.
 *   - It includes both personal and professional data such as name,
 *     contact, job details, and attendance.
 *   - The field 'loginRole' is used to identify whether the logged-in
 *     user is an Admin, HR, or Employee.
 *
 * Fields :
 *   id                      → Unique identifier for the employee
 *   firstName, lastName     → Employee’s personal details
 *   email, mobile           → Contact information
 *   cardNumber              → Unique ID/card number of the employee
 *   jobRole                 → Employee’s designation (e.g., Developer, HR)
 *   domain                  → Area of expertise (e.g., Java, Python, Cloud)
 *   loginRole               → Role of user in system (Admin / HR / Employee)
 *   gender                  → Gender information of the employee
 *   joiningDate             → Date of joining the organization
 *   salary                  → Monthly salary of the employee
 *   daysPresent             → Number of days the employee has attended work
 *   paidLeaves              → Number of paid leaves taken or available
 *
 * Result :
 *   This DTO is used mainly after login or profile retrieval
 *   to send complete employee data to the frontend.
 * ---------------------------------------------------------------
 */

@Data
public class EmployeeResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String cardNumber;//newly added
    private String jobRole;
    private String domain;
    private String loginRole;
    private String gender;
    private LocalDate joiningDate;
    private Double salary;
    private int daysPresent;
    private int paidLeaves;
    
}
