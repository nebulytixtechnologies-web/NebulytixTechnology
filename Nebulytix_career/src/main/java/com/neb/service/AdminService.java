/**
 * ---------------------------------------------------------------------
 * File Name   : AdminService.java
 * Package     : com.neb.service
 * ---------------------------------------------------------------------
 * Purpose :
 *   This interface defines the services (operations) that an Admin can perform
 *   in the application. It acts as a contract for the service layer.
 *
 * Description :
 *   The AdminService interface includes methods for:
 *     - Logging in as an admin
 *     - Adding new employees
 *     - Viewing all employee details
 *     - Assigning work to employees
 *     - Viewing all assigned works
 *     - Viewing works assigned to a specific employee
 *
 * Each method returns a response DTO (Data Transfer Object) that contains
 * the required information for the frontend or other layers.
 *
 * ---------------------------------------------------------------------
 * Methods :
 *
 * 1. EmployeeResponseDto login(LoginRequestDto loginReq)
 *      → Used for admin login authentication.
 *
 * 2. AddEmployeeResponseDto addEmployee(AddEmployeeRequestDto addEmpReq)
 *      → Adds a new employee and returns basic employee details.
 *
 * 3. List<EmployeeDetailsResponseDto> getEmployeeList()
 *      → Retrieves the list of all employees with their information.
 *
 * 4. WorkResponseDto assignWork(AddWorkRequestDto request)
 *      → Assigns a new task or project to an employee.
 *
 * 5. List<WorkResponseDto> getAllWorks()
 *      → Returns all the works/tasks assigned to employees.
 *
 * 6. List<WorkResponseDto> getWorkByEmployee(Long empId)
 *      → Fetches all works assigned to a particular employee using their ID.
 *
 * ---------------------------------------------------------------------
 * Usage :
 *   This interface is implemented by a class (e.g., AdminServiceImpl)
 *   which provides the actual business logic for each operation.
 * ---------------------------------------------------------------------
 */

package com.neb.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.neb.dto.*;

public interface AdminService {

    // For admin login verification
    public EmployeeResponseDto login(LoginRequestDto loginReq);

    // For adding a new employee
    public AddEmployeeResponseDto addEmployee(AddEmployeeRequestDto addEmpReq);

    // For retrieving all employee details
    public List<EmployeeDetailsResponseDto> getEmployeeList();

    // For assigning new work to an employee
    public String assignWork(AddWorkRequestDto request,MultipartFile file);

    // For fetching all assigned works to employee
    public List<WorkResponseDto> getAllWorks(Long empId);

    // For fetching work details of a specific employee
    public List<WorkResponseDto> getWorkByEmployee(Long empId);
    
    // Get employee details by ID
    public EmployeeDetailsResponseDto getEmployee(Long id);

    
    public String deleteHr(Long id);
    
    public UpdateEmployeeResponseDto updateHrDetails(Long empId,UpdateEmployeeRequestDto updateReq);
}
