/**
 * ---------------------------------------------------------------------
 * File Name   : EmployeeService.java
 * Package     : com.neb.service
 * ---------------------------------------------------------------------
 * Purpose :
 *   This interface defines the services (operations) that an employee 
 *   can perform in the application.
 *
 * Description :
 *   The EmployeeService interface includes methods for:
 *     - Employee login
 *     - Generating employee payslips
 *     - Viewing employee details
 *     - Getting assigned tasks
 *     - Submitting task reports
 *     - Fetching employee details by email
 *
 * Each method provides a specific functionality to manage 
 * employee-related activities within the system.
 *
 * ---------------------------------------------------------------------
 * Methods :
 *
 * 1. EmployeeResponseDto login(LoginRequestDto loginReq)
 *      → Authenticates an employee’s login credentials.
 *
 * 2. Payslip generatePayslip(Long employeeId, String monthYear)
 *      → Creates a payslip for the given employee and month.
 *
 * 3. Employee getEmployeeById(Long id)
 *      → Retrieves employee details using their unique ID.
 *
 * 4. List<Work> getTasksByEmployee(Long employeeId)
 *      → Returns all tasks assigned to a specific employee.
 *
 * 5. Work submitReport(Long taskId, String reportDetails, LocalDate submittedDate)
 *      → Allows the employee to submit a report for a completed task.
 *
 * 6. EmployeeDetailsResponseDto getEmployeeByEmail(String email)
 *      → Fetches employee information using their email ID.
 *
 * ---------------------------------------------------------------------
 * Usage :
 *   This interface is implemented by a class (e.g., EmployeeServiceImpl)
 *   that provides the actual business logic for each method.
 * ---------------------------------------------------------------------
 */

package com.neb.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.neb.dto.*;
import com.neb.entity.*;

public interface EmployeeService {

    // Login verification for employee
    public EmployeeResponseDto login(LoginRequestDto loginReq);

    // Generate payslip for a specific employee and month
    public Payslip generatePayslip(Long employeeId, String monthYear) throws Exception;

    // Get employee details by ID
    public Employee getEmployeeById(Long id);

    // Get all tasks assigned to an employee
    public List<Work> getTasksByEmployee(Long employeeId);

    // Submit task report after completion
    public WorkResponseDto submitReport(Long taskId, String status, String reportDetails, MultipartFile reportAttachment, LocalDate submittedDate);

    // Get employee details by email
    public EmployeeDetailsResponseDto getEmployeeByEmail(String email);
}
