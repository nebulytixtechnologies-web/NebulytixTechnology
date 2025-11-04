package com.neb.service.impl;

import java.time.LocalDate;
//original 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neb.constants.WorkStatus;
import com.neb.dto.AddEmployeeRequestDto;
import com.neb.dto.AddEmployeeResponseDto;
import com.neb.dto.AddWorkRequestDto;
import com.neb.dto.EmployeeDetailsResponseDto;
import com.neb.dto.EmployeeResponseDto;
import com.neb.dto.LoginRequestDto;
import com.neb.dto.UpdateEmployeeRequestDto;
import com.neb.dto.UpdateEmployeeResponseDto;
import com.neb.dto.WorkResponseDto;
import com.neb.entity.Employee;
import com.neb.entity.Work;
import com.neb.exception.CustomeException;
import com.neb.repo.EmployeeRepository;
import com.neb.repo.WorkRepository;
import com.neb.service.AdminService;

/**
 * ---------------------------------------------------------------
 * File Name   : AdminServiceImpl.java
 * Package     : com.neb.service.impl
 * ---------------------------------------------------------------
 * Purpose :
 *   This class provides the business logic implementation for
 *   administrative operations such as login, employee management,
 *   and work (task) assignment.
 *
 * Description :
 *   - Implements the AdminService interface.
 *   - Handles employee CRUD operations and assigns tasks to employees.
 *   - Uses repositories for database interaction and ModelMapper
 *     for converting entities to DTOs and vice versa.
 *
 * Dependencies :
 *   - EmployeeRepository → To perform database operations on Employee entities.
 *   - WorkRepository     → To manage Work (task) entities.
 *   - ModelMapper        → For mapping between entity and DTO objects.
 *
 * Result :
 *   Provides a complete implementation for admin-related operations,
 *   ensuring clean separation between service logic and data access.
 * ---------------------------------------------------------------
 */
@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
    private EmployeeRepository empRepo;
	
	@Autowired
    private WorkRepository workRepo;

    @Autowired
    private ModelMapper mapper;
    
    /**
     * Validates login credentials for Admin, HR, or Employee.
     * 
     * @param loginReq contains email, password, and login role
     * @return EmployeeResponseDto with employee details if valid
     * @throws CustomeException if credentials are invalid
     */
    
     // --------- LOGIN ----------
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
    
    /**
     * Adds a new employee to the system.
     * 
     * @param addEmpReq details of the new employee
     * @return AddEmployeeResponseDto containing saved employee data
     * @throws CustomeException if an employee with the same email already exists
     */
    // --------- ADD EMPLOYEE ----------
    @Override
    public AddEmployeeResponseDto addEmployee(AddEmployeeRequestDto addEmpReq) {

        // check if employee with same email already exists
        if (empRepo.existsByEmail(addEmpReq.getEmail())) {
        	throw new CustomeException("Admin with email :" + addEmpReq.getEmail() + " already exists");
        }

        // map DTO to entity
        Employee emp = mapper.map(addEmpReq, Employee.class);

        // save entity
        Employee savedEmp = empRepo.save(emp);

        // map saved entity to response DTO
        AddEmployeeResponseDto addEmpRes = mapper.map(savedEmp, AddEmployeeResponseDto.class);

        return addEmpRes;
    }
     
    /**
     * Retrieves a list of all employees (excluding admin).
     * 
     * @return list of EmployeeDetailsResponseDto
     * @throws CustomeException if no employees are found
     */
     //  ----------Get Employee List-------------
	@Override
	public List<EmployeeDetailsResponseDto> getEmployeeList() {
		
		//getting all employee list without admin
	    List<Employee> employeeList = empRepo.findByLoginRoleNot("admin");
	    
	    if(employeeList==null) {
	    	throw new CustomeException("Employees not found");
	    }
	    
	    List<EmployeeDetailsResponseDto> empListRes = employeeList.stream().map(emp->{
	    	
	    	EmployeeDetailsResponseDto empResDto = mapper.map(emp, EmployeeDetailsResponseDto.class);
	    	return empResDto;
	    }).collect(Collectors.toList());
	    
	    return empListRes;
	}
	
	
               //............. adding work ..............
	 /**
     * Assigns a new work/task to an employee.
     * 
     * @param request contains task details and employee ID
     * @return WorkResponseDto with assigned task details
     * @throws CustomeException if the employee ID is invalid
     */
	
    public WorkResponseDto assignWork(AddWorkRequestDto request) {
        Employee emp = empRepo.findById(request.getEmployeeId())
                .orElseThrow(() -> new CustomeException("Employee not found with id :"+request.getEmployeeId()));

        Work work = new Work();
        work.setTitle(request.getTitle());
        work.setDescription(request.getDescription());
        work.setAssignedDate(LocalDate.now());
        work.setDueDate(request.getDueDate());
        work.setStatus(WorkStatus.ASSIGNED);
        work.setEmployee(emp);

        Work savedWork= workRepo.save(work);
        WorkResponseDto workRes = mapToDto(savedWork);

        return workRes;
    }

    /**
     * Fetches all assigned work/tasks.
     * 
     * @return list of WorkResponseDto
     * @throws CustomeException if no works are found
     */ 
    public List<WorkResponseDto> getAllWorks() {
    	List<Work> allWork = workRepo.findAll();
    	if(allWork==null) {
    		throw new CustomeException("works not found");
    	}
        return allWork
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    	
    }

    /**
     * Fetches all works assigned to a specific employee.
     * 
     * @param empId employee ID
     * @return list of WorkResponseDto
     * @throws CustomeException if no works are found for the employee
     */
    public List<WorkResponseDto> getWorkByEmployee(Long empId) {
    	
    	List<Work> workListByEmployeeId = workRepo.findByEmployeeId(empId);
    	if(workListByEmployeeId==null) {
    		throw new CustomeException("works not found for employee with employee id :"+empId);
    	}
        return workListByEmployeeId
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    
    }
    
    /**
     * Helper method to convert Work entity to WorkResponseDto.
     * 
     * @param work Work entity
     * @return mapped WorkResponseDto
     */
    
    private WorkResponseDto mapToDto(Work work) {
        WorkResponseDto dto = new WorkResponseDto();
        dto.setId(work.getId());
        dto.setTitle(work.getTitle());
        dto.setDescription(work.getDescription());
        dto.setAssignedDate(work.getAssignedDate());
        dto.setDueDate(work.getDueDate());
        dto.setStatus(work.getStatus());
        dto.setReportDetails(work.getReportDetails());
        dto.setSubmittedDate(work.getSubmittedDate());
        dto.setEmployeeId(work.getEmployee().getId());
        dto.setEmployeeName(work.getEmployee().getFirstName() + " " + work.getEmployee().getLastName());
        dto.setEmployeeEmail(work.getEmployee().getEmail());
        return dto;
    }

	@Override
	public String deleteHr(Long id) {
		
		Optional<Employee> emp = empRepo.findById(id);	
		if(emp.isPresent()) {
			empRepo.deleteById(id);
			return "Hr deleted with id:"+id;
		}
		else {
			throw new CustomeException("Hr not found with id :"+id);
		}
	}

	@Override
	public UpdateEmployeeResponseDto updateHrDetails(Long empId, UpdateEmployeeRequestDto updateReq) {
		
		Employee hr = empRepo.findById(empId).orElseThrow(()->new CustomeException("hr not found with id :"+empId));
		hr.setFirstName(updateReq.getFirstName());
		hr.setLastName(updateReq.getLastName());
		hr.setEmail(updateReq.getEmail());
		hr.setMobile(updateReq.getMobile());
		hr.setCardNumber(updateReq.getCardNumber());
		hr.setLoginRole(updateReq.getLoginRole());
		hr.setJobRole(updateReq.getJobRole());
		hr.setDomain(updateReq.getDomain());
		hr.setGender(updateReq.getGender());
		hr.setJoiningDate(updateReq.getJoiningDate());
		hr.setSalary(updateReq.getSalary());
		hr.setDaysPresent(updateReq.getDaysPresent());
		hr.setPaidLeaves(updateReq.getPaidLeaves());
		hr.setBankAccountNumber(updateReq.getBankAccountNumber());
		hr.setBankName(updateReq.getBankName());
		hr.setPfNumber(updateReq.getPfNumber());
		hr.setPanNumber(updateReq.getPanNumber());
		hr.setUanNumber(updateReq.getUanNumber());
		hr.setEpsNumber(updateReq.getEpsNumber());
		hr.setEsiNumber(updateReq.getEsiNumber());
		
		Employee emp = empRepo.save(hr);
		UpdateEmployeeResponseDto updateHrRes = mapper.map(emp, UpdateEmployeeResponseDto.class);
		return updateHrRes;
	}
	@Override
	public EmployeeDetailsResponseDto getEmployee(Long id) {

		Employee emp = empRepo.findById(id).orElseThrow(()->new CustomeException("Employee not founce wuith id :"+id));
		return mapper.map(emp, EmployeeDetailsResponseDto.class);
		
	}
	
	}
