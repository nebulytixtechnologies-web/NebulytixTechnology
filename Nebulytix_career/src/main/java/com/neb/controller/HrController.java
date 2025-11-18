package com.neb.controller;

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
import com.neb.dto.UpdateEmployeeRequestDto;
import com.neb.dto.UpdatePasswordRequestDto;
import com.neb.entity.Payslip;
import com.neb.service.EmployeeService;
import com.neb.service.HrService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/hr")
@CrossOrigin(origins = "http://localhost:5173")
public class HrController {

    @Autowired
    private HrService service;

    @Autowired
    private EmployeeService employeeService;

    /** ------------------------------
     *  HR LOGIN  (CREATES SESSION)
     *  ------------------------------
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<EmployeeResponseDto>> login(
            @RequestBody LoginRequestDto loginReq,
            HttpSession session) {

        EmployeeResponseDto loginRes = service.login(loginReq);

        session.setAttribute("userId", loginRes.getId());
        session.setAttribute("role", "HR");
        session.setAttribute("email", loginRes.getEmail());

        return ResponseEntity.ok(
                new ResponseMessage<>(HttpStatus.OK.value(), HttpStatus.OK.name(),
                        "HR login successfully", loginRes));
    }

    /** ------------------------------
     *  SESSION VALIDATION FOR HR
     *  ------------------------------
     */
    private ResponseEntity<ResponseMessage<String>> validateHr(HttpSession session) {
        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("HR")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseMessage<>(401, "UNAUTHORIZED",
                            "Access denied! Please login as HR.", null));
        }
        return null;
    }

    /** ADD EMPLOYEE */
    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody AddEmployeeRequestDto addEmpReq,
                                         HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        AddEmployeeResponseDto addEmpRes = service.addEmployee(addEmpReq);
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee added successfully", addEmpRes));
    }

    /** GET EMPLOYEE LIST */
    @GetMapping("/getEmpList")
    public ResponseEntity<?> getEmployeeList(HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        List<EmployeeDetailsResponseDto> employeeList = service.getEmployeeList();
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "All employees fetched successfully", employeeList));
    }

    /** GET ONE EMPLOYEE */
    @GetMapping("/getEmp/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id,
                                         HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        EmployeeDetailsResponseDto employee = service.getEmployee(id);
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee fetched successfully", employee));
    }

    /** DELETE EMPLOYEE */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id,
                                            HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        String deleteById = service.deleteById(id);
        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee deleted successfully", deleteById));
    }

    /** DOWNLOAD PAYSLIP */
    @GetMapping("/payslip/{id}/download")
    public ResponseEntity<?> download(@PathVariable Long id,
                                      HttpSession session) throws Exception {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        byte[] pdf = service.downloadPayslip(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("payslip_" + id + ".pdf")
                .build());

        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    /** LIST ALL PAYSLIPS FOR EMPLOYEE */
    @GetMapping("/payslip/{employeeId}")
    public ResponseEntity<?> listPayslips(@PathVariable Long employeeId,
                                          HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        List<PayslipDto> payslips = service.listPayslipsForEmployee(employeeId);
        return ResponseEntity.ok(payslips);
    }

    /** GENERATE PAYSLIP */
    @PostMapping("/payslip/generate")
    public ResponseEntity<?> generate(@RequestBody GeneratePayslipRequest request,
                                      HttpSession session) throws Exception {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        Payslip p = employeeService.generatePayslip(request.getEmployeeId(), request.getMonthYear());
        PayslipDto dto = PayslipDto.fromEntity(p);

        return ResponseEntity.ok(dto);
    }

    /** UPDATE ATTENDANCE */
    @PutMapping("/editEmp/{empId}/{days}")
    public ResponseEntity<?> addAttendence(@PathVariable Long empId,
                                           @PathVariable int days,
                                           HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        EmployeeDetailsResponseDto updatedEmp = service.addAttendence(empId, days);

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee details updated", updatedEmp));
    }

    /** UPDATE EMPLOYEE DETAILS */
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage<EmployeeDetailsResponseDto>> updateEmployee(
            @PathVariable Long id,
            @RequestBody UpdateEmployeeRequestDto updateReq) {

        EmployeeDetailsResponseDto updatedEmp = service.updateEmployee(id, updateReq);

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Employee updated successfully", updatedEmp));
    }

    /** UPDATE PASSWORD */
    @PutMapping("/updatePassword/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable Long id,
                                            @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto,
                                            HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        EmployeeDetailsResponseDto updatedEmp = service.updatePassword(id, updatePasswordRequestDto);

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Password updated successfully", updatedEmp));
    }

    /** UPDATE BANK DETAILS */
    @PutMapping("/updateBankDetails/{id}")
    public ResponseEntity<?> updateBankDetails(@PathVariable Long id,
                                               @RequestBody UpdateBankDetailsRequestDto bankDetailsDto,
                                               HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        EmployeeDetailsResponseDto updatedEmp = service.updateBankDetails(id, bankDetailsDto);

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Bank details updated successfully", updatedEmp));
    }

    /** ADD JOB */
    @PostMapping("/addJob")
    public ResponseEntity<?> addJob(@RequestBody AddJobRequestDto jobRequest,
                                    HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        JobDetailsDto jobRes = service.addJob(jobRequest);

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "Job added successfully", jobRes));
    }

    /** GET ALL JOBS */
    @GetMapping("/allJobs")
    public ResponseEntity<?> getJobList(HttpSession session) {

        ResponseEntity<ResponseMessage<String>> validation = validateHr(session);
        if (validation != null) return validation;

        List<JobDetailsDto> allJobs = service.getAllJobs();

        return ResponseEntity.ok(
                new ResponseMessage<>(200, "OK", "All jobs fetched successfully", allJobs));
    }
}
