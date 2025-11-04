package com.neb.dto;

import lombok.Data;

/**
 * ---------------------------------------------------------------
 * File Name   : LoginRequestDto.java
 * Package     : com.neb.dto
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used to capture and transfer login credentials
 *   entered by the user during the authentication process.
 *
 * Description :
 *   - It works as a Data Transfer Object (DTO) between the frontend
 *     and backend for login requests.
 *   - It carries the user’s email, password, and login role
 *     (like admin, HR, or employee).
 *
 * Fields :
 *   email      → Registered email ID of the user
 *   password   → User’s account password used for authentication
 *   loginRole  → Defines the role of the user logging in 
 *                 (e.g., admin/hr/employee)
 *
 * Result :
 *   This DTO helps in securely transferring login information
 *   from the frontend to the backend during user authentication.
 * ---------------------------------------------------------------
 */

@Data
public class LoginRequestDto {
    private String email;
    private String password;
    private String loginRole; // admin/hr/employee
}
