/**
 * ---------------------------------------------------------------------
 * File Name   : HrService.java
 * Package     : com.neb.service
 * ---------------------------------------------------------------------
 * Purpose :
 *   This interface defines the operations that the HR (Human Resources)
 *   department can perform in the application.
 *
 * Description :
 *   The HrService interface includes methods for:
 *     - HR login
 *     - Adding, updating, and deleting employees
 *     - Viewing employee details
 *     - Managing attendance
 *     - Handling payslips (listing and downloading)
 *
 * Each method in this interface helps the HR team manage 
 * employee records, attendance, and salary slips.
 *
 * ---------------------------------------------------------------------
 * Methods :
 *
 * 1. AddEmployeeResponseDto addEmployee(AddEmployeeRequestDto addEmpReq)
 *      → Adds a new employee to the system.
 *
 * 2. EmployeeResponseDto login(LoginRequestDto loginReq)
 *      → Allows HR to log in using credentials.
 *
 * 3. List<EmployeeDetailsResponseDto> getEmployeeList()
 *      → Retrieves the complete list of employees.
 *
 * 4. EmployeeDetailsResponseDto getEmployee(Long id)
 *      → Fetches a specific employee’s details using their ID.
 *
 * 5. String deleteById(Long id)
 *      → Deletes an employee record from the database.
 *
 * 6. byte[] downloadPayslip(Long payslipId)
 *      → Downloads the payslip file (PDF) for a given payslip ID.
 *
 * 7. List<PayslipDto> listPayslipsForEmployee(Long employeeId)
 *      → Retrieves all payslips belonging to a particular employee.
 *
 * 8. EmployeeDetailsResponseDto addAttendence(Long id, int days)
 *      → Adds or updates the attendance record (days present) for an employee.
 *
 * 9. EmployeeDetailsResponseDto updateEmployee(Long id, AddEmployeeRequestDto updateReq)
 *      → Updates an employee’s information such as role, domain, etc.
 *
 * ---------------------------------------------------------------------
 * Usage :
 *   This interface is implemented by a class (e.g., HrServiceImpl)
 *   which contains the actual business logic for these operations.
 * ---------------------------------------------------------------------
 */

package com.neb.service;

import java.util.List;

import com.neb.dto.*;

public interface HrService {

    // Add a new employee
    public AddEmployeeResponseDto addEmployee(AddEmployeeRequestDto addEmpReq);

    // HR login
    public EmployeeResponseDto login(LoginRequestDto loginReq);

    // Get all employee details
    public List<EmployeeDetailsResponseDto> getEmployeeList();

    // Get employee details by ID
    public EmployeeDetailsResponseDto getEmployee(Long id);

    // Delete employee by ID
    public String deleteById(Long id);

    // Download an employee's payslip (PDF)
    public byte[] downloadPayslip(Long payslipId) throws Exception;

    // List all payslips for a specific employee
    public List<PayslipDto> listPayslipsForEmployee(Long employeeId);

    // Add or update employee attendance
    public EmployeeDetailsResponseDto addAttendence(Long id, int days);

    // Update existing employee information
    EmployeeDetailsResponseDto updateEmployee(Long id, AddEmployeeRequestDto updateReq);
    
    EmployeeDetailsResponseDto updatePassword(Long id, UpdatePasswordRequestDto updatePasswordRequestDto);
    EmployeeDetailsResponseDto updateBankDetails(Long id, UpdateBankDetailsRequestDto bankDetailsDto);


}
