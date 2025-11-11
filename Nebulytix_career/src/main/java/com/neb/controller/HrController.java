package com.neb.controller;
//HrController
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neb.dto.AddEmployeeRequestDto;
import com.neb.dto.AddEmployeeResponseDto;
import com.neb.dto.AddJobRequestDto;
import com.neb.dto.EmployeeDetailsResponseDto;
import com.neb.dto.EmployeeResponseDto;
import com.neb.dto.GeneratePayslipRequest;
import com.neb.dto.JobDetailsDto;
import com.neb.dto.LoginRequestDto;
import com.neb.dto.PayslipDto;
import com.neb.dto.ResponseMessage;
import com.neb.dto.UpdateBankDetailsRequestDto;
import com.neb.dto.UpdatePasswordRequestDto;
import com.neb.entity.Payslip;
import com.neb.service.EmployeeService;
import com.neb.service.HrService;

/**
 *  HrController manages all HR-related operations within the AdminHrEmpDashBoards system.
 *
 * This includes:
 *    -- HR login
 *    -- Adding and updating employees
 *    -- Deleting employee records
 *    -- Managing attendance
 *    -- Generating and downloading payslips
 * 
 *
 *  All endpoints are exposed under /api/hr and support CORS for frontend
 * interaction from <b>http://localhost:5173.
 */
@RestController
@RequestMapping("/api/hr")
@CrossOrigin(origins = "http://localhost:5173")
public class HrController {

	@Autowired
	private HrService service;
	
	@Autowired
	private EmployeeService employeeService;
	
	/**
     * Handles HR login requests.
     *
     * @param loginReq the login credentials (email, password)
     * @return a response containing HR details and success message
     */
	@PostMapping("/login")
	public ResponseEntity<ResponseMessage<EmployeeResponseDto>> login(@RequestBody LoginRequestDto loginReq){
		
		EmployeeResponseDto loginRes = service.login(loginReq);
		
		return ResponseEntity.ok(new ResponseMessage<EmployeeResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), "Hr login successfully", loginRes));
	}
	
	/**
     * Adds a new employee to the system.
     *
     * @param addEmpReq the employee details to be added
     * @return a response with confirmation and added employee details
     */
	//adding employee
	@PostMapping("/add")
	public ResponseEntity<ResponseMessage<AddEmployeeResponseDto>> addEmployee(@RequestBody AddEmployeeRequestDto addEmpReq){
		
		System.out.println(addEmpReq);
		AddEmployeeResponseDto addEmpRes = service.addEmployee(addEmpReq);
		
		return ResponseEntity.ok(new ResponseMessage<AddEmployeeResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), "employee added successfully", addEmpRes));
	}
	
	/**
     * Fetches the list of all employees.
     *
     * @return a list of all employees wrapped in a response message
     */
	@GetMapping("/getEmpList")
	public ResponseEntity<ResponseMessage<List<EmployeeDetailsResponseDto>>> getEmployeeList(){
		
		List<EmployeeDetailsResponseDto> employeeList = service.getEmployeeList();
		
		return ResponseEntity.ok(new ResponseMessage<List<EmployeeDetailsResponseDto>>(HttpStatus.OK.value(), HttpStatus.OK.name(), "All Employee fetched successfully", employeeList));
	}
	
	 /**
     * Fetches the details of a specific employee by ID.
     *
     * @param id the ID of the employee
     * @return employee details if found
     */
	@GetMapping("/getEmp/{id}")
	public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> getEmployee(@PathVariable Long id){
		
		EmployeeDetailsResponseDto employee = service.getEmployee(id);
	
		
		return ResponseEntity.ok(new ResponseMessage<EmployeeDetailsResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), " Employee fetched successfully", employee));
	}
	
	/**
     * Deletes an employee record based on the provided ID.
     *
     * @param id the ID of the employee to delete
     * @return a success message upon deletion
     */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseMessage<String>> deleteEmployee(@PathVariable Long id){
		
	 String deleteById = service.deleteById(id);
	
		
		return ResponseEntity.ok(new ResponseMessage<String>(HttpStatus.OK.value(), HttpStatus.OK.name(), " Employee deleted successfully", deleteById));
	}
	
	 /**
     * Downloads an employee's payslip as a PDF file.
     *
     * @param id the ID of the payslip
     * @return a PDF file containing the payslip
     * @throws Exception if the payslip cannot be generated or retrieved
     */
	@GetMapping("/payslip/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) throws Exception {
        byte[] pdf = service.downloadPayslip(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
            .attachment()
            .filename("payslip_" + id + ".pdf")
            .build());

        return ResponseEntity.ok()
                             .headers(headers)
                             .body(pdf);
    }

    /**
     * Fetches all payslips of a specific employee.
     *
     * @param employeeId the employee's ID
     * @return list of payslips for the employee
     */
    @GetMapping("/payslip/{employeeId}")
    public ResponseEntity<List<PayslipDto>> listPayslips(@PathVariable Long employeeId) {
        List<PayslipDto> payslips = service.listPayslipsForEmployee(employeeId);
        return ResponseEntity.ok(payslips);
    }
    
    /**
     * Generates a new payslip for a specific employee and month.
     *
     * @param request contains employee ID and month-year
     * @return generated payslip details
     * @throws Exception if payslip generation fails
     */
    @PostMapping("/payslip/generate")
    public ResponseEntity<PayslipDto> generate(@RequestBody GeneratePayslipRequest request) throws Exception {
        Payslip p = employeeService.generatePayslip(request.getEmployeeId(), request.getMonthYear());
        PayslipDto dto = PayslipDto.fromEntity(p);
        return ResponseEntity.ok(dto);
    }
	 
    /**
     * Updates the attendance (number of working days) for an employee.
     *
     * @param empId the employee's ID
     * @param days  number of working days
     * @return updated employee details
     */
    @PutMapping("/editEmp/{empId}/{days}")
    public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> addAttendence(@PathVariable Long empId, @PathVariable int days){
    	
    	EmployeeDetailsResponseDto updatedEmp = service.addAttendence(empId, days);
    	
    	return ResponseEntity.ok(new ResponseMessage<EmployeeDetailsResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), "employee details updated", updatedEmp));
    }
    
    /**
     * Updates an employee’s personal or job-related details.
     *
     * @param id        the employee's ID
     * @param updateReq updated employee information
     * @return response containing the updated employee details
     */
    // updating employee
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> updateEmployee(
            @PathVariable Long id,
            @RequestBody AddEmployeeRequestDto updateReq) {

        EmployeeDetailsResponseDto updatedEmp = service.updateEmployee(id, updateReq);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "Employee details updated successfully",
                updatedEmp));
    }
    @PutMapping("/updatePassword/{id}")
    public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> updatePassword(
            @PathVariable Long id,
            @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {

        EmployeeDetailsResponseDto updatedEmp = service.updatePassword(id, updatePasswordRequestDto);

        return ResponseEntity.ok(
                new ResponseMessage<>(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        "Password updated successfully",
                        updatedEmp
                )
        );
    }
    @PutMapping("/updateBankDetails/{id}")
    public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> updateBankDetails(
            @PathVariable Long id,
            @RequestBody UpdateBankDetailsRequestDto bankDetailsDto) {

        EmployeeDetailsResponseDto updatedEmp = service.updateBankDetails(id, bankDetailsDto);

        return ResponseEntity.ok(
                new ResponseMessage<>(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        "Bank details updated successfully",
                        updatedEmp
                )
        );
    }
    
    @PostMapping("/addJob")
    public ResponseEntity<ResponseMessage<JobDetailsDto>> addJob(@RequestBody AddJobRequestDto jobRequest) {
        JobDetailsDto jobRes = service.addJob(jobRequest);
        return ResponseEntity.ok(
                new ResponseMessage<>(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        "Job added successfully",
                        jobRes
                )
        );
    }
    
    @GetMapping("/allJobs")
    public ResponseEntity<ResponseMessage<List<JobDetailsDto>>> getJobList() {
        List<JobDetailsDto> allJobs = service.getAllJobs();
        return ResponseEntity.ok(new ResponseMessage<List<JobDetailsDto>>(HttpStatus.OK.value(), HttpStatus.OK.name(), "all jobs fetched successfully", allJobs));
       
    }
}
