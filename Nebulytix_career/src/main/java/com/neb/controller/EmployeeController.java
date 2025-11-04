package com.neb.controller;
//original

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neb.dto.EmployeeDetailsResponseDto;
import com.neb.dto.EmployeeResponseDto;
import com.neb.dto.GeneratePayslipRequest;
import com.neb.dto.LoginRequestDto;
import com.neb.dto.PayslipDto;
import com.neb.dto.ResponseMessage;
import com.neb.entity.Employee;
import com.neb.entity.Payslip;
import com.neb.entity.Work;
import com.neb.service.EmployeeService;

/**
 * =====================================================
 *                   EmployeeController
 * =====================================================
 * 
 * 
 * This REST controller manages all endpoints related to 
 * employee operations in the NEB HR Management System.
 * 
 * 
 * Responsibilities:
 *    -- Handle employee authentication
 *    -- Provide employee details and assigned work
 *    -- Generate monthly payslips
 *    -- Allow employees to submit work reports
 * 
 * 
 *
 * This controller acts as an interface between the front-end 
 * (React/Vue/Angular) and the service layer {@link EmployeeService}.
 * 
 * 
 * 
 * CORS is enabled for requests originating from 
 *  http://localhost:5173, which is typically your frontend development server.
 * 
 */
@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {
	/** Injected service layer dependency for employee operations */
	@Autowired
	private EmployeeService employeeService;
	
	 /**
     * Handles employee login requests.
     *
     * @param loginReq DTO containing login credentials (email and password)
     * @return A ResponseEntity with login success message and employee details
     */
	@PostMapping("/login")
	public ResponseEntity<ResponseMessage<EmployeeResponseDto>> login(@RequestBody LoginRequestDto loginReq){
		
		EmployeeResponseDto loginRes = employeeService.login(loginReq);
		
		return ResponseEntity.ok(new ResponseMessage<EmployeeResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), "Employee login successfully", loginRes));
	}
	
	/**
     * Generates a payslip for an employee for a specific month and year.
     *
     * @param request DTO containing employee ID and month-year for which payslip is to be generated
     * @return A ResponseEntity with the generated PayslipDto
     * @throws Exception If payslip generation fails or employee not found
     */
	@PostMapping("/payslip/generate")
    public ResponseEntity<PayslipDto> generate(@RequestBody GeneratePayslipRequest request) throws Exception {
        Payslip p = employeeService.generatePayslip(request.getEmployeeId(), request.getMonthYear());
        PayslipDto dto = PayslipDto.fromEntity(p);
        return ResponseEntity.ok(dto);
    }
	
	 /**
     * Fetches employee details by their ID.
     *
     * @param id The unique ID of the employee
     * @return A ResponseMessage containing employee entity details
     */
	 // Get employee details
    @GetMapping("/get/{id}")
    public ResponseMessage<Employee> getEmployee(@PathVariable Long id) {
        Employee emp = employeeService.getEmployeeById(id);
        return new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(), "Employee fetched successfully", emp);
    }
    
    /**
     * Fetches employee details by their email address.
     *
     * @param email Employee’s email address
     * @return A ResponseEntity with employee details or NOT_FOUND if no employee exists with the given email
     */
    @GetMapping("/details/{email}")
    public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> getEmployeeByEmail(@PathVariable String email) {
    	EmployeeDetailsResponseDto emp = employeeService.getEmployeeByEmail(email);	
        if (emp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(404, "NOT_FOUND", "Employee not found"));
        }
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee fetched successfully", emp)
        );
    }
    
    /**
     * Retrieves the list of tasks assigned to a specific employee.
     *
     * @param employeeId The ID of the employee
     * @return A ResponseMessage containing the list of Work objects assigned to the employee
     */
    // Get tasks assigned to employee
    @GetMapping("/tasks/{employeeId}")
    public ResponseMessage<List<Work>> getTasks(@PathVariable Long employeeId) {
        List<Work> tasks = employeeService.getTasksByEmployee(employeeId);
        return new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(), "Tasks fetched successfully", tasks);
    }
    
    /**
     * Submits a report for a specific assigned task.
     *
     * @param taskId The ID of the task being reported
     * @param reportData Work object containing report details submitted by the employee
     * @return A ResponseMessage containing updated Work information after report submission
     */
    // Submit task report
    @PutMapping("/task/submit/{taskId}")
    public ResponseMessage<Work> submitTaskReport(
            @PathVariable Long taskId,
            @RequestBody Work reportData
    ) {
        Work updatedTask = employeeService.submitReport(taskId, reportData.getReportDetails(),  LocalDate.now());
        return new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(), "Report submitted successfully", updatedTask);
    }
    

	 	
}
