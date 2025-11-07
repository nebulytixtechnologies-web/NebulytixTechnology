package com.neb.dto;



/**
 * ------------------------------------------------------------
 * File Name   : UpdatePasswordRequestDto.java
 * Package     : com.neb.dto
 * ------------------------------------------------------------
 * Purpose :
 *   This DTO carries data required for updating an employee’s
 *   password based on employee ID.
 *
 * Fields :
 *   - password : the new password to set
 *
 * ------------------------------------------------------------
 */

public class UpdatePasswordRequestDto {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

