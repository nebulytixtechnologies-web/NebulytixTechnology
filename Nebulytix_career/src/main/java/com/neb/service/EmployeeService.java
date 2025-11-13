package com.neb.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.neb.dto.*;
import com.neb.entity.Employee;
import com.neb.entity.Payslip;
import com.neb.entity.Work;

public interface EmployeeService {

    EmployeeResponseDto login(LoginRequestDto loginReq);

    Payslip generatePayslip(Long empId, String monthYear) throws Exception;

    Employee getEmployeeById(Long id);

    EmployeeDetailsResponseDto getEmployeeByEmail(String email);

    List<Work> getTasksByEmployee(Long employeeId);

    WorkResponseDto submitReport(Long taskId, String status, String reportDetails, MultipartFile reportAttachment,
            java.time.LocalDate submissionDate);

    EmployeeDetailsResponseDto updateEmployeeByHr(Long id, UpdateEmployeeRequestDto dto);
}
