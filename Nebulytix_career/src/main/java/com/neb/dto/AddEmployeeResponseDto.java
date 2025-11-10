/**
 * ---------------------------------------------------------------
 * File Name   : AddEmployeeResponseDto.java
 * Package     : com.neb.dto
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used to send a response back to the frontend
 *   after a new employee is successfully added to the system.
 *
 * Description :
 *   - It is a Data Transfer Object (DTO) that holds only the 
 *     important details of the newly created employee.
 *   - It helps confirm that the employee has been added and 
 *     provides key information like ID, name, email, and role.
 *
 * Fields :
 *   id          → Unique employee ID generated after saving
 *   firstName   → Employee’s first name
 *   email       → Employee’s email address
 *   loginRole   → Type of user (e.g., hr or employee)
 *   jobRole     → Employee’s job position
 *   password    → Employee’s login password
 *   cardNumber  → Employee’s card or ID number
 *
 * Result :
 *   Sends basic employee details back to the frontend 
 *   after successful registration.
 * ---------------------------------------------------------------
 */

package com.neb.dto;

import lombok.Data;

@Data
public class AddEmployeeResponseDto {
    private Long id;
    private String firstName;
    private String email;
    private String jobRole;
    private String password;
    private String cardNumber;
}
