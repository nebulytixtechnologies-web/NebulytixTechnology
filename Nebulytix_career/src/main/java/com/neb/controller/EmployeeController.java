package com.neb.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.neb.dto.EmployeeDetailsResponseDto;
import com.neb.dto.EmployeeResponseDto;
import com.neb.dto.GeneratePayslipRequest;
import com.neb.dto.LoginRequestDto;
import com.neb.dto.PayslipDto;
import com.neb.dto.ResponseMessage;
import com.neb.dto.WorkResponseDto;
import com.neb.entity.Employee;
import com.neb.entity.Payslip;
import com.neb.entity.Work;
import com.neb.service.EmployeeService;
import com.neb.util.SessionUtil;

import jakarta.servlet.http.HttpSession;

/**
 * =====================================================
 *                   EmployeeController
 * =====================================================
 */
@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ============================================================
    //  LOGIN — session added here
    // ============================================================
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<EmployeeResponseDto>> login(
            @RequestBody LoginRequestDto loginReq,
            HttpSession session) {

        EmployeeResponseDto loginRes = employeeService.login(loginReq);

        //  MODIFIED — storing session values
        session.setAttribute("userId", loginRes.getId());
        session.setAttribute("role", "EMPLOYEE");
        session.setAttribute("email", loginRes.getEmail());

        return ResponseEntity.ok(
                new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(),
                        "Employee login successfully", loginRes));
    }

    // ============================================================
    //  Generate Payslip — session check added
    // ============================================================
    @PostMapping("/payslip/generate")
    public ResponseEntity<?> generate(
            @RequestBody GeneratePayslipRequest request,
            HttpSession session) throws Exception {

        // 🔥 MODIFIED — Session validation
        if (!SessionUtil.isLoggedIn(session) || !SessionUtil.isEmployee(session)) {
            return ResponseEntity.status(401)
                    .body("Unauthorized: Employee session not found or expired");
        }

        Payslip p = employeeService.generatePayslip(request.getEmployeeId(), request.getMonthYear());
        PayslipDto dto = PayslipDto.fromEntity(p);
        return ResponseEntity.ok(dto);
    }

    // ============================================================
    //  Get employee details — session check added
    // ============================================================
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getEmployee(
            @PathVariable Long id,
            HttpSession session) {

        if (!SessionUtil.isLoggedIn(session) || !SessionUtil.isEmployee(session)) {
            return ResponseEntity.status(401)
                    .body("Unauthorized: Employee session not found or expired");
        }

        Employee emp = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(
                new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(),
                        "Employee fetched successfully", emp));
    }

    // ============================================================
    //  Get employee by email — session check added
    // ============================================================
    @GetMapping("/details/{email}")
    public ResponseEntity<?> getEmployeeByEmail(
            @PathVariable String email,
            HttpSession session) {

        if (!SessionUtil.isLoggedIn(session) || !SessionUtil.isEmployee(session)) {
            return ResponseEntity.status(401)
                    .body("Unauthorized: Employee session not found or expired");
        }

        EmployeeDetailsResponseDto emp = employeeService.getEmployeeByEmail(email);
        if (emp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(404, "NOT_FOUND", "Employee not found"));
        }

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee fetched successfully", emp));
    }

    // ============================================================
    //  Get tasks — session check added
    // ============================================================
    @GetMapping("/tasks/{employeeId}")
    public ResponseEntity<?> getTasks(
            @PathVariable Long employeeId,
            HttpSession session) {

        if (!SessionUtil.isLoggedIn(session) || !SessionUtil.isEmployee(session)) {
            return ResponseEntity.status(401)
                    .body("Unauthorized: Employee session not found or expired");
        }

        List<Work> tasks = employeeService.getTasksByEmployee(employeeId);
        return ResponseEntity.ok(
                new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(),
                        "Tasks fetched successfully", tasks));
    }

    // ============================================================
    //  Submit report — session check added
    // ============================================================
    @PutMapping("/task/submit/{taskId}")
    public ResponseEntity<?> submitTaskReport(
            @PathVariable Long taskId,
            @RequestParam("status") String status,
            @RequestParam("reportDetails") String reportDetails,
            @RequestParam(value = "reportAttachment", required = false) MultipartFile reportAttachment,
            HttpSession session) {

        if (!SessionUtil.isLoggedIn(session) || !SessionUtil.isEmployee(session)) {
            return ResponseEntity.status(401)
                    .body("Unauthorized: Employee session not found or expired");
        }

        WorkResponseDto updatedTask = employeeService.submitReport(
                taskId, status, reportDetails, reportAttachment, LocalDate.now());

        ResponseMessage<WorkResponseDto> response = new ResponseMessage<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "Report submitted successfully",
                updatedTask
        );

        return ResponseEntity.ok(response);
    }

    // ============================================================
    //LOGOUT — added
    // ============================================================
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();   //  MODIFIED
        return ResponseEntity.ok("Employee logged out successfully");
    }
}
