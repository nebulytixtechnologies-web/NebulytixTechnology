package com.neb.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.neb.dto.EmployeeDetailsResponseDto;
import com.neb.dto.EmployeeResponseDto;
import com.neb.dto.GeneratePayslipRequest;
import com.neb.dto.LoginRequestDto;
import com.neb.dto.PayslipDto;
import com.neb.dto.ResponseMessage;
import com.neb.dto.UpdateEmployeeRequestDto;
import com.neb.dto.WorkResponseDto;
import com.neb.entity.Employee;
import com.neb.entity.Payslip;
import com.neb.entity.Work;
import com.neb.service.EmployeeService;

@RestController
@RequestMapping("/api/employees") // ✅ changed to plural
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /** ✅ Employee Login */
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<EmployeeResponseDto>> login(@RequestBody LoginRequestDto loginReq) {
        EmployeeResponseDto loginRes = employeeService.login(loginReq);
        return ResponseEntity.ok(
                new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(),
                        "Employee login successfully", loginRes));
    }

    /** ✅ Generate Payslip */
    @PostMapping("/payslip/generate")
    public ResponseEntity<PayslipDto> generate(@RequestBody GeneratePayslipRequest request) throws Exception {
        Payslip p = employeeService.generatePayslip(request.getEmployeeId(), request.getMonthYear());
        PayslipDto dto = PayslipDto.fromEntity(p);
        return ResponseEntity.ok(dto);
    }

    /** ✅ Get Employee by ID */
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseMessage<Employee>> getEmployee(@PathVariable Long id) {
        Employee emp = employeeService.getEmployeeById(id);
        if (emp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(404, "NOT_FOUND", "Employee not found"));
        }
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee fetched successfully", emp));
    }

    /** ✅ Get Employee by Email */
    @GetMapping("/details/{email}")
    public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> getEmployeeByEmail(@PathVariable String email) {
        EmployeeDetailsResponseDto emp = employeeService.getEmployeeByEmail(email);
        if (emp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(404, "NOT_FOUND", "Employee not found"));
        }
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee fetched successfully", emp));
    }

    /** ✅ Get Tasks assigned to Employee */
    @GetMapping("/tasks/{employeeId}")
    public ResponseMessage<List<Work>> getTasks(@PathVariable Long employeeId) {
        List<Work> tasks = employeeService.getTasksByEmployee(employeeId);
        return new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(),
                "Tasks fetched successfully", tasks);
    }

    /** ✅ Submit Task Report */
    @PutMapping("/task/submit/{taskId}")
    public ResponseEntity<ResponseMessage<WorkResponseDto>> submitTaskReport(
            @PathVariable Long taskId,
            @RequestParam("status") String status,
            @RequestParam("reportDetails") String reportDetails,
            @RequestParam(value = "reportAttachment", required = false) MultipartFile reportAttachment) {

        WorkResponseDto updatedTask = employeeService.submitReport(taskId, status, reportDetails, reportAttachment,
                LocalDate.now());
        return ResponseEntity.ok(
                new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(),
                        "Report submitted successfully", updatedTask));
    }

    /** ✅ HR Update Employee Details (No Bank Fields) */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> updateEmployeeByHr(
            @PathVariable Long id, @RequestBody UpdateEmployeeRequestDto dto) {

        EmployeeDetailsResponseDto updated = employeeService.updateEmployeeByHr(id, dto);

        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(404, "NOT_FOUND", "Employee not found"));
        }

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee details updated successfully", updated));
    }
}
