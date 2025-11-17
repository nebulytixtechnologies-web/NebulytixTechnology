// 🔹 Admin Controller
package com.neb.controller;

import java.io.IOException;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
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

// 🔥 ADDED FOR SESSION MGMT
import com.neb.util.SessionUtil;

import jakarta.servlet.http.HttpSession;

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


    // -------------------------------------------------------------
    //  ADMIN LOGIN  (Modified to add session values)
    // -------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<EmployeeResponseDto>> login(
            @RequestBody LoginRequestDto loginReq,
            HttpSession session) {

        EmployeeResponseDto loginRes = adminService.login(loginReq);

        // 🔥 ADDED FOR SESSION MGMT
        session.setAttribute("userId", loginRes.getId());
        session.setAttribute("role", "ADMIN");
        session.setAttribute("email", loginRes.getEmail());

        return ResponseEntity.ok(
                new ResponseMessage<>(HttpStatus.OK.value(), "OK",
                        "Admin login successfully", loginRes));
    }


    // -------------------------------------------------------------
    //  ADD HR  (Session check added)
    // -------------------------------------------------------------
    @PostMapping("/addhr")
    public ResponseEntity<?> addEmployee(
            @RequestBody AddEmployeeRequestDto addEmpReq,
            HttpSession session) {

        //  SESSION CHECK
        if (!SessionUtil.isAdmin(session)) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Admin session not found or expired", null));
        }

        AddEmployeeResponseDto addEmpRes = adminService.addEmployee(addEmpReq);
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "HR added successfully", addEmpRes));
    }


    // -------------------------------------------------------------
    // GET ALL EMPLOYEES  (Session check added)
    // -------------------------------------------------------------
    @GetMapping("/getEmpList")
    public ResponseEntity<?> getEmployeeList(HttpSession session) {

        //  SESSION CHECK
        if (!SessionUtil.isAdmin(session)) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Admin session not found or expired", null));
        }

        List<EmployeeDetailsResponseDto> employeeList = adminService.getEmployeeList();

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "All Employee fetched successfully", employeeList));
    }


    // -------------------------------------------------------------
    //  ASSIGN WORK  (Should be secured but session was missing)
    // -------------------------------------------------------------
    @PostMapping(value = "/work/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseMessage<String>> addWork(
            @RequestPart("dto") AddWorkRequestDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpSession session) throws IOException {

        //  SESSION CHECK
        if (!SessionUtil.isAdmin(session)) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Admin session not found or expired", null));
        }

        String workRes = adminService.assignWork(dto, file);

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "Work added successfully", workRes));
    }


    // -------------------------------------------------------------
    //  GET ALL WORK (Session check added)
    // -------------------------------------------------------------
    @GetMapping("/getAllWork/{empId}")
    public ResponseEntity<?> getAllWork(@PathVariable Long empId, HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Admin session expired", null));
        }

        List<WorkResponseDto> works = adminService.getAllWorks(empId);
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "All work fetched successfully", works));
    }


    @GetMapping("/getWork/{empId}")
    public ResponseEntity<?> getWorkByEmployee(@PathVariable Long empId, HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Admin session expired", null));
        }

        List<WorkResponseDto> works = adminService.getWorkByEmployee(empId);
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "Employee work fetched", works));
    }


    // -------------------------------------------------------------
    // DELETE HR
    // -------------------------------------------------------------
    @DeleteMapping("/delete/hr/{id}")
    public ResponseEntity<?> deleteHr(@PathVariable Long id, HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Admin session expired", null));
        }

        String deleteRes = adminService.deleteHr(id);
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "HR deleted successfully", deleteRes));
    }


    @PutMapping("/update/hr/{id}")
    public ResponseEntity<?> updateHr(
            @PathVariable Long id,
            @RequestBody UpdateEmployeeRequestDto updateReq,
            HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Admin session expired", null));
        }

        UpdateEmployeeResponseDto updatedhrRes = adminService.updateHrDetails(id, updateReq);

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "HR details updated successfully", updatedhrRes));
    }


    @GetMapping("/getEmp/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id, HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Admin session expired", null));
        }

        EmployeeDetailsResponseDto employee = adminService.getEmployee(id);

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "Employee fetched successfully", employee));
    }


    // -------------------------------------------------------------
    // PAYSLIP (Admin is allowed)
    // -------------------------------------------------------------
    @GetMapping("/payslip/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) throws Exception {

        byte[] pdf = hrService.downloadPayslip(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("payslip_" + id + ".pdf").build());

        return ResponseEntity.ok().headers(headers).body(pdf);
    }


    @GetMapping("/payslip/{employeeId}")
    public ResponseEntity<List<PayslipDto>> listPayslips(@PathVariable Long employeeId) {
        return ResponseEntity.ok(hrService.listPayslipsForEmployee(employeeId));
    }


    @PostMapping("/payslip/generate")
    public ResponseEntity<PayslipDto> generate(@RequestBody GeneratePayslipRequest request)
            throws Exception {

        Payslip p = employeeService.generatePayslip(request.getEmployeeId(),
                request.getMonthYear());
        return ResponseEntity.ok(PayslipDto.fromEntity(p));
    }


    // -------------------------------------------------------------
    // ATTENDANCE
    // -------------------------------------------------------------
    @PutMapping("/editEmp/{empId}/{days}")
    public ResponseEntity<?> addAttendence(
            @PathVariable Long empId,
            @PathVariable int days,
            HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Admin session expired", null));
        }

        EmployeeDetailsResponseDto updatedEmp = hrService.addAttendence(empId, days);

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "Employee details updated", updatedEmp));
    }


    @GetMapping("/reports/daily")
    public ResponseEntity<byte[]> generateReport(
            @RequestParam LocalDate submittedDate) throws Exception {

        byte[] pdfBytes = adminService.generateDailyReport(submittedDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("DailyReport_" + submittedDate + ".pdf").build());

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }


    // -------------------------------------------------------------
    //  LOGOUT (NEWLY ADDED)
    // -------------------------------------------------------------
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK",
                        "Admin logged out successfully", null));
    }
}
