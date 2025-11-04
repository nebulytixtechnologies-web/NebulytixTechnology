package com.neb.dto;

import java.time.LocalDate;

import lombok.Data;

/**
 * ---------------------------------------------------------------
 * File Name   : EmployeeDetailsResponseDto.java
 * Package     : com.neb.dto
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used to send detailed information about an employee
 *   from the backend to the frontend after retrieval or update operations.
 *
 * Description :
 *   - It functions as a Data Transfer Object (DTO) that carries 
 *     employee details required for display or processing.
 *   - It encapsulates both personal and professional data such as
 *     name, contact details, job information, salary, and attendance.
 *
 * Fields :
 *   id                      → Unique identifier for the employee
 *   firstName, lastName     → Employee’s personal name details
 *   email, mobile           → Contact information
 *   cardNumber              → Unique card/ID number for employee tracking
 *   jobRole                 → Employee’s designation (e.g., Developer, HR)
 *   domain                  → Area of expertise (e.g., Java, Python, .NET)
 *   gender                  → Gender information of the employee
 *   joiningDate             → Date when the employee joined the organization
 *   salary                  → Monthly salary assigned to the employee
 *   daysPresent             → Number of days the employee was present
 *   paidLeaves              → Number of paid leave days available or taken
 *
 * Result :
 *   This DTO helps in transferring complete employee details
 *   to the frontend for displaying in employee lists, profiles,
 *   or HR dashboards.
 * ---------------------------------------------------------------
 */

@Data
public class EmployeeDetailsResponseDto {

	private Long id;
	private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String cardNumber;//newly added
    private String jobRole;     // intern/developer/cloud engineer/hr
    private String domain;      // Java / .Net / Python
    private String gender;
    private LocalDate joiningDate;
    private Double salary;
    private int daysPresent;
    private int paidLeaves;
    private String loginRole;
   
    
}
