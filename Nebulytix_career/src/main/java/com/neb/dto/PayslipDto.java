package com.neb.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.neb.entity.Employee;
import com.neb.entity.Payslip;

import lombok.Data;

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
 *   - It serves as a Data Transfer Object (DTO) between the frontend
 *     and backend during the employee registration process.
 *   - It holds all necessary details about an employee including
 *     personal, professional, salary, and bank information.
 *
 * Fields :
 *   firstName, lastName               → Basic employee identity details
 *   email, mobile, cardNumber         → Contact and identification information
 *   loginRole                         → Specifies the user type (admin/hr/employee)
 *   jobRole, domain                   → Professional role and work domain
 *   joiningDate, salary               → Employment details
 *   daysPresent, paidLeaves           → Attendance and leave information
 *   password                          → Used for authentication
 *   bankAccountNumber, bankName       → Bank details
 *   pfNumber, panNumber,
 *   uanNumber, epsNumber, esiNumber   → Government ID and statutory details
 *
 * Result :
 *   This DTO enables smooth transfer of all employee registration data
 *   from the frontend to the backend service for processing and storage.
 * ---------------------------------------------------------------
 */

@Data
public class PayslipDto {

    private Long id;
    private String payslipMonth;
    private LocalDateTime generatedDate;

    // salary breakdown
    private Double basic;
    private Double hra;
    private Double flexi;
    private Double grossSalary;

    // deductions breakdown
    private Double pfDeduction;
    private Double profTaxDeduction;
    private Double totalDeductions;

    private Double netSalary;

    // file info
    private String fileName;
    // Optional: you may or may not expose pdfPath
    private String pdfPath;

    // employee details
    private Long employeeId;
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeEmail;
    private String employeeBankAccountNumber;
    private String employeeBankName;
    private String employeePfNumber;
    private String employeePanNumber;
    private String employeeUanNumber;
    
    //newly added
    private String location;
    private Double balance;
    private Double aggrgDeduction;
    private Double incHdSalary;      // note: camelCase, maybe rename from “IncHdSalary”
    private Double taxCredit;

    // mapping from entity
    public static PayslipDto fromEntity(Payslip p) {
        PayslipDto dto = new PayslipDto();
        dto.setId(p.getId());
        dto.setPayslipMonth(p.getPayslipMonth());
        dto.setGeneratedDate(p.getGeneratedDate());
        dto.setBasic(p.getBasic());
        dto.setHra(p.getHra());
        dto.setFlexi(p.getFlexi());
        dto.setGrossSalary(p.getGrossSalary());
        dto.setPfDeduction(p.getPfDeduction());
        dto.setProfTaxDeduction(p.getProfTaxDeduction());
        dto.setTotalDeductions(p.getTotalDeductions());
        dto.setNetSalary(p.getNetSalary());
        dto.setFileName(p.getFileName());
        dto.setPdfPath(p.getPdfPath());
        
        dto.setLocation(p.getLocation());
        dto.setBalance(p.getBalance());
        dto.setAggrgDeduction(p.getAggrgDeduction());
        dto.setIncHdSalary(p.getIncHdSalary());
        dto.setTaxCredit(p.getTaxCredit());


        if (p.getEmployee() != null) {
            Employee emp = p.getEmployee();
            dto.setEmployeeId(emp.getId());
            dto.setEmployeeFirstName(emp.getFirstName());
            dto.setEmployeeLastName(emp.getLastName());
            dto.setEmployeeEmail(emp.getEmail());
            dto.setEmployeeBankAccountNumber(emp.getBankAccountNumber());
            dto.setEmployeeBankName(emp.getBankName());
            dto.setEmployeePfNumber(emp.getPfNumber());
            dto.setEmployeePanNumber(emp.getPanNumber());
            dto.setEmployeeUanNumber(emp.getUanNumber());
        }

        return dto;
    }
}
