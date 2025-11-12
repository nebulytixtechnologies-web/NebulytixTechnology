//admin controller
package com.neb.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.neb.dto.AddEmployeeRequestDto;
import com.neb.dto.AddEmployeeResponseDto;
import com.neb.dto.AddWorkRequestDto;
import com.neb.dto.EmployeeDetailsResponseDto;
import com.neb.dto.EmployeeResponseDto;
import com.neb.dto.GeneratePayslipRequest;
import com.neb.dto.LoginRequestDto;
import com.neb.dto.PayslipDto;
import com.neb.dto.ResponseMessage;
import com.neb.dto.UpdateEmployeeRequestDto;
import com.neb.dto.UpdateEmployeeResponseDto;
import com.neb.dto.WorkResponseDto;
import com.neb.entity.Payslip;
import com.neb.service.AdminService;
import com.neb.service.EmployeeService;
import com.neb.service.HrService;

/**
 * AdminController handles all administrative operations for the AdminHrEmpDashBoards system.
 * 
 * This includes:
 *  -- Admin login
 *  -- HR employee management
 *  -- Work assignment and tracking
 *  -- Admin add HR Detail
 * 
 * All endpoints are CORS-enabled for access from the frontend at http://localhost:5173.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private HrService hrService;
	
	@Autowired
	private EmployeeService employeeService;
	
	 /**
     * Handles admin login requests.
     * 
     * @param loginReq Contains admin login credentials (email and password).
     * @return Response with admin details and login success message.
     */
	@PostMapping("/login")
	public ResponseEntity<ResponseMessage<EmployeeResponseDto>> login(@RequestBody LoginRequestDto loginReq){
		
		EmployeeResponseDto loginRes = adminService.login(loginReq);
		
		return ResponseEntity.ok(new ResponseMessage<EmployeeResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), "admin login successfully", loginRes));
	}
	
	/**
     * Adds a new HR employee to the system.
     * 
     * @param addEmpReq Contains HR details such as name, email, and role.
     * @return Response confirming successful addition of HR.
     */
	//for adding Hr
	@PostMapping("/addhr")
	public ResponseEntity<ResponseMessage<AddEmployeeResponseDto>> addEmployee(@RequestBody AddEmployeeRequestDto addEmpReq){
		
		AddEmployeeResponseDto addEmpRes = adminService.addEmployee(addEmpReq);
		
		return ResponseEntity.ok(new ResponseMessage<AddEmployeeResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), "Hr added successfully", addEmpRes));
	}
	
	/**
     * Fetches a list of all employees in the organization.
     * 
     * @return Response containing a list of employee details.
     */
	//get employee list
	@GetMapping("/getEmpList")
	public ResponseEntity<ResponseMessage<List<EmployeeDetailsResponseDto>>> getEmployeeList(){
		
		List<EmployeeDetailsResponseDto> employeeList = adminService.getEmployeeList();
		
		return ResponseEntity.ok(new ResponseMessage<List<EmployeeDetailsResponseDto>>(HttpStatus.OK.value(), HttpStatus.OK.name(), "All Employee fetched successfully", employeeList));
	}
	     /**
	     * Assigns work to an employee.
	     * 
	     * @param req Work assignment request containing employee ID and task details.
	     * @return Response confirming work assignment success.
	     */
	 @PostMapping(value = "/work/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	 
	    public ResponseEntity<ResponseMessage<String>> addWork(
	        @RequestPart("dto") AddWorkRequestDto dto,
	        @RequestPart(value = "file", required = false) MultipartFile file
	    ) throws IOException {

	        String workRes = adminService.assignWork(dto, file);

	        return ResponseEntity.ok(
	            new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(), "Work added successfully", workRes)
	        );
	    }
        
	    /**
	     * Retrieves all assigned work across all employees.
	     * 
	     * @return Response containing a list of all work entries.
	     */
	    // ✅ Get all Work of employee
	    @GetMapping("/getAllWork/{empId}")
	    public ResponseEntity<ResponseMessage<List<WorkResponseDto>>> getAllWork(@PathVariable Long empId) {
	        List<WorkResponseDto> works = adminService.getAllWorks(empId);
	        return ResponseEntity.ok(new ResponseMessage<>(200, "OK", "All work fetched successfully", works));
	    }
	    
	    /**
	     * Retrieves all work assigned to a specific employee.
	     * 
	     * @param empId The ID of the employee whose work is to be fetched.
	     * @return Response containing a list of work assigned to the employee.
	     */
	    // ✅ Get Employee Work
	    @GetMapping("/getWork/{empId}")
	    public ResponseEntity<ResponseMessage<List<WorkResponseDto>>> getWorkByEmployee(@PathVariable Long empId) {
	        List<WorkResponseDto> works = adminService.getWorkByEmployee(empId);
	        return ResponseEntity.ok(new ResponseMessage<>(200, "OK", "Work fetched for employee", works));
	    }
	    
	    @DeleteMapping("/delete/hr/{id}")//http://localhost:5054/api/admin/delete/hr/3
	    public ResponseEntity<ResponseMessage<?>> deleteHr(@PathVariable Long id){
	    	String deleteRes = adminService.deleteHr(id);
	    	return ResponseEntity.ok(new ResponseMessage<>(200, "OK", "hr deleted successfully", deleteRes));
	    }
	    
	    @PutMapping("/update/hr/{id}")//http://localhost:5054/api/admin/update/hr/4
		public ResponseEntity<ResponseMessage<UpdateEmployeeResponseDto>> updateHr(@PathVariable Long id,@RequestBody UpdateEmployeeRequestDto updateReq){
			
			UpdateEmployeeResponseDto updatedhrRes = adminService.updateHrDetails(id, updateReq);
			
			return ResponseEntity.ok(new ResponseMessage<UpdateEmployeeResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), "hr details updated successfully", updatedhrRes));
		}
	    
	    @GetMapping("/getEmp/{id}")
		public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> getEmployee(@PathVariable Long id){
			
			EmployeeDetailsResponseDto employee = adminService.getEmployee(id);
		
			
			return ResponseEntity.ok(new ResponseMessage<EmployeeDetailsResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), " Employee fetched successfully", employee));
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
	        byte[] pdf = hrService.downloadPayslip(id);

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
	        List<PayslipDto> payslips = hrService.listPayslipsForEmployee(employeeId);
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
	    	
	    	EmployeeDetailsResponseDto updatedEmp = hrService.addAttendence(empId, days);
	    	
	    	return ResponseEntity.ok(new ResponseMessage<EmployeeDetailsResponseDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), "employee details updated", updatedEmp));
	    }
	    
	 
}
