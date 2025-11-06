package com.neb.service.impl;
//original
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.neb.dto.EmployeeDetailsResponseDto;
import com.neb.dto.EmployeeResponseDto;
import com.neb.dto.LoginRequestDto;
import com.neb.dto.SubmitTaskReportDto;
import com.neb.entity.Employee;
import com.neb.entity.Payslip;
import com.neb.entity.Work;
import com.neb.exception.CustomeException;
import com.neb.repo.EmployeeRepository;
import com.neb.repo.PayslipRepository;
import com.neb.repo.WorkRepository;
import com.neb.service.EmployeeService;
import com.neb.util.PdfGeneratorUtil;

/**
 * ---------------------------------------------------------------
 * File Name   : EmployeeServiceImpl.java
 * Package     : com.neb.service.impl
 * ---------------------------------------------------------------
 * Purpose :
 *   This class implements the business logic for employee-related 
 *   operations such as authentication, payslip generation, 
 *   task retrieval, and report submission.
 *
 * Description :
 *   - Implements the EmployeeService interface.
 *   - Handles login validation, payslip creation (including 
 *     PDF generation and storage), and employee work management.
 *   - Uses repositories for database interaction and ModelMapper 
 *     for mapping between entities and DTOs.
 *
 * Dependencies :
 *   - EmployeeRepository → For performing CRUD operations on employees.
 *   - PayslipRepository  → For storing and retrieving payslip data.
 *   - WorkRepository     → For handling employee task details.
 *   - ModelMapper        → For entity-to-DTO conversion.
 *   - PdfGeneratorUtil   → For generating employee payslip PDFs.
 *
 * Result :
 *   Provides a full implementation for employee functionalities, 
 *   ensuring smooth integration between backend data and UI components.
 * ---------------------------------------------------------------
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository empRepo;
    
    @Autowired
    private PayslipRepository payslipRepo;

    @Autowired
    private ModelMapper mapper;
    
    @Autowired
    private WorkRepository workRepository;

    
    @Value("${payslip.base-folder}")
    private String baseFolder;

    // --------- LOGIN ----------
    /**
     * Validates the login credentials for employees, HR, or admins.
     *
     * @param loginReq contains email, password, and login role
     * @return EmployeeResponseDto containing the logged-in employee’s details
     * @throws CustomeException if credentials are invalid
     */
    @Override
    public EmployeeResponseDto login(LoginRequestDto loginReq) {

        // fetch employee from DB
        Employee emp = empRepo.findByEmailAndPasswordAndLoginRole(
                loginReq.getEmail(),
                loginReq.getPassword(),
                loginReq.getLoginRole()
        ).orElseThrow(() -> new CustomeException("Invalid credentials. Please check your email and password and login role"));

        // map entity to DTO
        EmployeeResponseDto loginRes = mapper.map(emp, EmployeeResponseDto.class);

        return loginRes;
    }
    
    
    //Getting employee By ID
    /**
     * Retrieves employee details by employee ID.
     *
     * @param id the employee’s unique ID
     * @return Employee entity corresponding to the given ID
     * @throws CustomeException if employee is not found
     */
    public Employee getEmployeeById(Long id) {
        return empRepo.findById(id).orElseThrow(() -> new CustomeException("Employee not found with id: "+id));
    }
    
    /**
     * Generates a payslip for the given employee and month.
     *
     * The method calculates salary components like basic, HRA, flexi, 
     * deductions, and net salary. It then generates a PDF payslip and 
     * saves it to the configured directory.
     * 
     *
     * @param employeeId ID of the employee for whom payslip is generated
     * @param monthYear  the month and year for which the payslip is created
     * @return Payslip entity containing salary breakdown and PDF file info
     * @throws Exception if file generation or saving fails
     * @throws CustomeException if employee is not found
     */
	@Override
	public Payslip generatePayslip(Long employeeId, String monthYear) throws Exception{
		
		
		Employee emp = empRepo.findById(employeeId)
	            .orElseThrow(() -> new CustomeException("Employee not found with id: "+employeeId));
		
		Payslip p = new Payslip();
        p.setEmployee(emp);
        p.setPayslipMonth(monthYear);
        p.setGeneratedDate(LocalDateTime.now());
        p.setLocation("FLAT NO 501B,PSR PRIME TOWERS,BESIDE DLF,GACHIBOWLI,500032");

        // Salary Calculations
        double salary = emp.getSalary();
        p.setBasic(salary * 0.53);
        p.setHra(salary * 0.20);
        p.setFlexi(salary * 0.27);
        double gross = p.getBasic() + p.getHra() + p.getFlexi();//
        p.setGrossSalary(gross);
        
        // Deductions
        p.setPfDeduction(p.getBasic() * 0.12);
        p.setProfTaxDeduction(200.0);
        double ded = p.getPfDeduction() + p.getProfTaxDeduction();
        p.setTotalDeductions(ded);
        
        // Net Salary Calculation
        double net = gross - ded;
        p.setNetSalary(net);
        p.setBalance(gross);
        p.setAggrgDeduction(ded);
        p.setIncHdSalary(net);
        p.setTaxCredit(net*0.05);//random values added
     
        // Save payslip record
        p = payslipRepo.save(p);
        
        // PDF File Generation
        String fileName = emp.getCardNumber() + "_payslip" + monthYear.replace(" ", "_") + ".pdf";
        String folderPath = baseFolder + "/" + monthYear.replace(" ", "_");
        Files.createDirectories(Paths.get(folderPath));
        String fullPath = folderPath + "/" + fileName;

        
        byte[] pdfBytes = PdfGeneratorUtil.createPayslipPdf(emp, p);
        Files.write(Paths.get(fullPath), pdfBytes);

        p.setPdfPath(fullPath);
        p.setFileName(fileName);
        payslipRepo.save(p);

        return p;
	}
	 // Get employee details by EMAIL
	 /**
     * Retrieves employee details using their email ID.
     *
     * @param email employee email address
     * @return EmployeeDetailsResponseDto containing employee information
     * @throws CustomeException if employee with given email is not found
     */
    public EmployeeDetailsResponseDto getEmployeeByEmail(String email) {
    	System.out.println(email);
    	Employee emp = empRepo.findByEmail(email).orElseThrow(()->new CustomeException("Employee not found with email id :"+email));
    	EmployeeDetailsResponseDto empdetailsDto = mapper.map(emp, EmployeeDetailsResponseDto.class);
        return empdetailsDto;
    }
   
    /**
     * Fetches all work/tasks assigned to a specific employee.
     *
     * @param employeeId the employee ID
     * @return list of Work entities assigned to the employee
     * @throws CustomeException if no work is found for the employee
     */
    public List<Work> getTasksByEmployee(Long employeeId) {
        Employee emp = getEmployeeById(employeeId);
        List<Work> workListbyEmployee = workRepository.findByEmployee(emp);
        if(workListbyEmployee==null) {
        	throw new CustomeException("work list is empty for employee with id: "+emp.getId());
        }
        return workListbyEmployee;
    }

    /**
     * Submits a task report for the given task ID.
     *
     * Updates the work status to COMPLETED and stores the report details 
     * along with the submitted date.
     * 
     * @param taskId ID of the task being submitted
     * @param reportDetails report or description submitted by the employee
     * @param submittedDate date when the report was submitted
     * @return updated Work entity with completion details
     * @throws CustomeException if task is not found
     */
    public Work submitReport(Long taskId, SubmitTaskReportDto report, LocalDate submittedDate) {
        Work task = workRepository.findById(taskId).orElseThrow(() -> new CustomeException("Task not found with taskId :"+taskId));
        task.setReportDetails(report.getReportDetails());
        task.setSubmittedDate(submittedDate);
        task.setStatus(report.getStatus());
        return workRepository.save(task);
    }
	 
}
