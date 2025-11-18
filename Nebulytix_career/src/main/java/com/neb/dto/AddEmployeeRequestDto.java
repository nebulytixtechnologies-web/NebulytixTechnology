
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
