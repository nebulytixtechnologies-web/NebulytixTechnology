package com.neb.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neb.dto.AddEmployeeRequestDto;
import com.neb.dto.AddEmployeeResponseDto;
import com.neb.dto.EmployeeDetailsResponseDto;
import com.neb.dto.EmployeeResponseDto;
import com.neb.dto.LoginRequestDto;
import com.neb.dto.PayslipDto;
import com.neb.dto.UpdateBankDetailsRequestDto;
import com.neb.dto.UpdatePasswordRequestDto;
import com.neb.entity.Employee;
import com.neb.entity.Payslip;
import com.neb.exception.CustomeException;
import com.neb.repo.EmployeeRepository;
import com.neb.repo.PayslipRepository;
import com.neb.service.HrService;

/**
 * ---------------------------------------------------------------
 * File Name   : HrServiceImpl.java
 * Package     : com.neb.service.impl
 * ---------------------------------------------------------------
 * Purpose :
 *   Implements the core business logic for HR-related operations 
 *   such as employee management, attendance tracking, payslip 
 *   handling, and HR authentication.
 *
 * Description :
 *   - This service handles all HR functionalities by implementing 
 *     the HrService interface.
 *   - Includes methods for adding, updating, retrieving, and deleting 
 *     employee records.
 *   - Provides functionality for managing payslips and employee attendance.
 *   - Integrates ModelMapper for DTO mapping and repositories for 
 *     database operations.
 *
 * Dependencies :
 *   - EmployeeRepository → For CRUD operations on Employee entities.
 *   - PayslipRepository  → For accessing and managing payslip data.
 *   - ModelMapper        → For mapping between DTOs and entity objects.
 *
 * Result :
 *   Enables HR staff to manage employee information efficiently, 
 *   ensuring seamless backend integration with frontend interfaces.
 * ---------------------------------------------------------------
 */

@Service
public class HrServiceImpl implements HrService{

	@Autowired
    private EmployeeRepository empRepo;
	
	@Autowired
	private PayslipRepository payslipRepo;

    @Autowired
    private ModelMapper mapper;
    
                             // --------- LOGIN ----------
    /**
     * Validates HR or employee login credentials.
     *
     * @param loginReq object containing email, password, and login role
     * @return EmployeeResponseDto with the authenticated employee’s details
     * @throws CustomeException if the provided credentials are invalid
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
    
	                                       // --------- ADD EMPLOYEE ----------
    /**
     * Adds a new employee to the system.
     *
     * @param addEmpReq DTO containing new employee information
     * @return AddEmployeeResponseDto containing saved employee details
     * @throws CustomeException if an employee with the same email already exists
     */
    @Override
    public AddEmployeeResponseDto addEmployee(AddEmployeeRequestDto addEmpReq) {

        // check if employee with same email already exists
        if (empRepo.existsByEmail(addEmpReq.getEmail())) {
        	 throw new CustomeException("Employee with email " + addEmpReq.getEmail() + " already exists");
        }

        // map DTO to entity
       
        Employee emp = mapper.map(addEmpReq, Employee.class);
        emp.setLoginRole("employee");

        // save entity
        Employee savedEmp = empRepo.save(emp);
      

        // map saved entity to response DTO
        AddEmployeeResponseDto addEmpRes = mapper.map(savedEmp, AddEmployeeResponseDto.class);

        return addEmpRes;
    }
                                       // --------- EMPLOYEE LIST SECTION ----------
    /**
     * Retrieves the list of all employees, excluding admin and HR roles.
     *
     * @return List of EmployeeDetailsResponseDto representing all employees
     * @throws CustomeException if no employee records are found
     */
    @Override
    public List<EmployeeDetailsResponseDto> getEmployeeList() {
		
		//getting all employee list without admin
	    List<Employee> employeeList = empRepo.findByLoginRoleNotIn(List.of("admin","hr"));
	    
	    if(employeeList==null) {
	    	 throw new CustomeException("Employees list not found");
	    }
	    
	    List<EmployeeDetailsResponseDto> empListRes = employeeList.stream().map(emp->{
	    	
	    	EmployeeDetailsResponseDto empResDto = mapper.map(emp, EmployeeDetailsResponseDto.class);
	    	return empResDto;
	    }).collect(Collectors.toList());
	    
	    return empListRes;
	}
                                // --------- GET SINGLE EMPLOYEE SECTION ----------
    /**
     * Fetches details of a specific employee by ID.
     *
     * @param id the employee ID
     * @return EmployeeDetailsResponseDto with employee information
     * @throws CustomeException if employee is not found
     */
	@Override
	public EmployeeDetailsResponseDto getEmployee(Long id) {

		Employee emp = empRepo.findById(id).orElseThrow(()->new CustomeException("Employee not founce wuith id :"+id));
		return mapper.map(emp, EmployeeDetailsResponseDto.class);
		
	}
	                          // ---------  DELETE EMPLOYEE SECTION ----------
	 /**
     * Deletes an employee record from the database.
     *
     * @param id the employee ID
     * @return confirmation message after deletion
     */
	@Override
	public String deleteById(Long id) {
		empRepo.deleteById(id);
		return id+" Employee Deleted Successfully";
	}
	
	                          // ---------  PAYSLIP DOWNLOAD SECTION ----------
	 /**
     * Downloads a payslip PDF file for a given payslip ID.
     *
     * @param payslipId ID of the payslip
     * @return byte array representing the payslip PDF
     * @throws Exception if file reading fails or payslip not found
     * @throws CustomeException if payslip does not exist
     */
	@Override
	 public byte[] downloadPayslip(Long payslipId) throws Exception {
        Payslip p = payslipRepo.findById(payslipId)
            .orElseThrow(() -> new CustomeException("Payslip not found"));

        Path path = Paths.get(p.getPdfPath());
        return Files.readAllBytes(path);
    }
	 
	 //getting list of payslips of employee using employee id
	
	 /**
     * Retrieves the list of payslips associated with a specific employee.
     *
     * @param employeeId the employee ID
     * @return list of PayslipDto representing payslip details
     * @throws CustomeException if payslip list is not found
     */
	@Override
     public List<PayslipDto> listPayslipsForEmployee(Long employeeId) {
        List<Payslip> payslips = payslipRepo.findByEmployeeId(employeeId);
        
        if(payslips==null) {
        	throw new CustomeException("payslip list is not found with employeeId: "+employeeId);
        }
        List<PayslipDto> paySlipDtos = payslips.stream()
                                        .map(PayslipDto::fromEntity)
                                        .toList();
        return paySlipDtos;
    }
	                              // ---------  ATTENDANCE SECTION  ----------
	 /**
     * Updates the attendance (days present) of a specific employee.
     *
     * @param id the employee ID
     * @param days the number of days the employee was present
     * @return EmployeeDetailsResponseDto containing updated attendance info
     * @throws CustomeException if employee not found
     */
	@Override
	public EmployeeDetailsResponseDto addAttendence(Long id, int days) {
		
		Employee emp = empRepo.findById(id).orElseThrow(()->new CustomeException("employee not found with id:"+id));
		emp.setDaysPresent(days);
		Employee savedemp = empRepo.save(emp);
		EmployeeDetailsResponseDto updateEmpDto= mapper.map(savedemp, EmployeeDetailsResponseDto.class);
		return updateEmpDto;
	}

                            // ---------- UPDATE EMPLOYEE DETAILS ----------
	 /**
     * Updates existing employee details in the system.
     *
     * @param id ID of the employee to be updated
     * @param updateReq DTO containing updated employee information
     * @return EmployeeDetailsResponseDto containing updated employee details
     * @throws CustomeException if employee not found
     */
    @Override
    public EmployeeDetailsResponseDto updateEmployee(Long id, AddEmployeeRequestDto updateReq) {
        Employee emp = empRepo.findById(id)
                .orElseThrow(() -> new CustomeException("Employee not found with id: " + id));

     // --- Update fields only if they are not null or empty ---
        if (updateReq.getFirstName() != null && !updateReq.getFirstName().isEmpty())
            emp.setFirstName(updateReq.getFirstName());
        if (updateReq.getLastName() != null && !updateReq.getLastName().isEmpty())
            emp.setLastName(updateReq.getLastName());
        if (updateReq.getEmail() != null && !updateReq.getEmail().isEmpty())
            emp.setEmail(updateReq.getEmail());
        if (updateReq.getMobile() != null && !updateReq.getMobile().isEmpty())
            emp.setMobile(updateReq.getMobile());
        if (updateReq.getCardNumber() != null && !updateReq.getCardNumber().isEmpty())
            emp.setCardNumber(updateReq.getCardNumber());
        if (updateReq.getJobRole() != null && !updateReq.getJobRole().isEmpty())
            emp.setJobRole(updateReq.getJobRole());
        if (updateReq.getDomain() != null && !updateReq.getDomain().isEmpty())
            emp.setDomain(updateReq.getDomain());
        if (updateReq.getGender() != null && !updateReq.getGender().isEmpty())
            emp.setGender(updateReq.getGender());
        if (updateReq.getJoiningDate() != null)
            emp.setJoiningDate(updateReq.getJoiningDate());
        if (updateReq.getSalary() != null)
            emp.setSalary(updateReq.getSalary());
        if (updateReq.getDaysPresent() != 0)
            emp.setDaysPresent(updateReq.getDaysPresent());
        if (updateReq.getPaidLeaves() != 0)
            emp.setPaidLeaves(updateReq.getPaidLeaves());
        if (updateReq.getPassword() != null && !updateReq.getPassword().isEmpty())
            emp.setPassword(updateReq.getPassword());
        
        // --- Bank & legal details ---
        if (updateReq.getBankAccountNumber() != null && !updateReq.getBankAccountNumber().isEmpty())
            emp.setBankAccountNumber(updateReq.getBankAccountNumber());
        if (updateReq.getBankName() != null && !updateReq.getBankName().isEmpty())
            emp.setBankName(updateReq.getBankName());
        if (updateReq.getPfNumber() != null && !updateReq.getPfNumber().isEmpty())
            emp.setPfNumber(updateReq.getPfNumber());
        if (updateReq.getPanNumber() != null && !updateReq.getPanNumber().isEmpty())
            emp.setPanNumber(updateReq.getPanNumber());
        if (updateReq.getUanNumber() != null && !updateReq.getUanNumber().isEmpty())
            emp.setUanNumber(updateReq.getUanNumber());
        if (updateReq.getEpsNumber() != null && !updateReq.getEpsNumber().isEmpty())
            emp.setEpsNumber(updateReq.getEpsNumber());
        if (updateReq.getEsiNumber() != null && !updateReq.getEsiNumber().isEmpty())
            emp.setEsiNumber(updateReq.getEsiNumber());

        Employee updatedEmp = empRepo.save(emp);

        return mapper.map(updatedEmp, EmployeeDetailsResponseDto.class);
    }
    
    @Override
    public EmployeeDetailsResponseDto updatePassword(Long id, UpdatePasswordRequestDto updatePasswordRequestDto) {
        // ✅ Find employee by ID
        Employee emp = empRepo.findById(id)
                .orElseThrow(() -> new CustomeException("Employee not found with id: " + id));

        // ✅ Update password
        emp.setPassword(updatePasswordRequestDto.getPassword());

        // ✅ Save updated employee
        Employee updatedEmp = empRepo.save(emp);

        // ✅ Return updated DTO
        return mapper.map(updatedEmp, EmployeeDetailsResponseDto.class);
    }

    @Override
    public EmployeeDetailsResponseDto updateBankDetails(Long id, UpdateBankDetailsRequestDto bankDetailsDto) {
        Employee emp = empRepo.findById(id)
                .orElseThrow(() -> new CustomeException("Employee not found with id: " + id));

        // Update only bank-related fields
        if (bankDetailsDto.getBankAccountNumber() != null && !bankDetailsDto.getBankAccountNumber().isEmpty())
            emp.setBankAccountNumber(bankDetailsDto.getBankAccountNumber());
        if (bankDetailsDto.getBankName() != null && !bankDetailsDto.getBankName().isEmpty())
            emp.setBankName(bankDetailsDto.getBankName());
        if (bankDetailsDto.getPfNumber() != null && !bankDetailsDto.getPfNumber().isEmpty())
            emp.setPfNumber(bankDetailsDto.getPfNumber());
        if (bankDetailsDto.getPanNumber() != null && !bankDetailsDto.getPanNumber().isEmpty())
            emp.setPanNumber(bankDetailsDto.getPanNumber());
        if (bankDetailsDto.getUanNumber() != null && !bankDetailsDto.getUanNumber().isEmpty())
            emp.setUanNumber(bankDetailsDto.getUanNumber());
        if (bankDetailsDto.getEpsNumber() != null && !bankDetailsDto.getEpsNumber().isEmpty())
            emp.setEpsNumber(bankDetailsDto.getEpsNumber());
        if (bankDetailsDto.getEsiNumber() != null && !bankDetailsDto.getEsiNumber().isEmpty())
            emp.setEsiNumber(bankDetailsDto.getEsiNumber());

        Employee updatedEmp = empRepo.save(emp);

        // Return all details (not just bank info)
        return mapper.map(updatedEmp, EmployeeDetailsResponseDto.class);
    }


}
