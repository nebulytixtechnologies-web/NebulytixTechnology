/**
 * ---------------------------------------------------------------
 * File Name   : AddEmployeeRequestDto.java
 * Package     : com.neb.dto
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used to collect and transfer employee details
 *   when a new employee is added to the system.
 *
 * Description :
 *   - It works as a Data Transfer Object (DTO) between the frontend
 *     and backend.
 *   - It holds all required information about an employee such as
 *     personal details, job details, salary, and bank details.
 *
 * Fields :
 *   firstName, lastName         → Employee’s basic information
 *   email, mobile, cardNumber   → Contact and ID details
 *   loginRole                   → Defines user type (admin/hr/employee)
 *   jobRole, domain             → Job information (like developer, Java)
 *   joiningDate, salary         → Employment details
 *   daysPresent, paidLeaves     → Attendance details
 *   password                    → Used for employee login
 *   bankAccountNumber, bankName → Bank details
 *   pfNumber, panNumber,
 *   uanNumber, epsNumber,
 *   esiNumber                   → Employee identification details
 *
 * Result :
 *   This DTO helps in sending all employee data together
 *   from the frontend to the backend during employee registration.
 * ---------------------------------------------------------------
 */

package com.neb.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AddEmployeeRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String cardNumber;

    private String loginRole;   // "hr" or "employee"
    private String jobRole;     // Required if loginRole = "employee"
    private String domain;      // Example: Java, .Net, Python
    private String gender;
    private LocalDate joiningDate;
    private Double salary;
    private int daysPresent;
    private int paidLeaves;
    private String password;
    
    
    private String bankAccountNumber;
    private String bankName;
    private String pfNumber;
    private String panNumber;
    private String uanNumber;
    private String epsNumber;
    private String esiNumber;
}
