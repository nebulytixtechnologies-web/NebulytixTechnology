package com.neb.controller;



import com.neb.scheduler.PayslipScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payslip")
public class PayslipSchedulerController {

    @Autowired
    private PayslipScheduler payslipScheduler;

    // 🔹 Trigger payslip generation manually
    @PostMapping("/generate-all")
    public ResponseEntity<String> generateAllPayslips() {
        payslipScheduler.generateMonthlyPayslips();
        return ResponseEntity.ok("Payslips generated successfully for all employees.");
    }
}

