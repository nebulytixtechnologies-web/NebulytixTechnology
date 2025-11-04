package com.neb.scheduler;

import com.neb.entity.Employee;
import com.neb.repo.EmployeeRepository;
import com.neb.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/**
 * -----------------------------------------------------------------
 * Class: PayslipScheduler
 * -----------------------------------------------------------------
 * Purpose:
 *   Automatically generates payslips for all employees on the
 *   1st day of every month at midnight.
 *
 * Schedule:
 *   CRON = "0 0 0 1 * *"
 *   → Runs at 12:00 AM on day 1 of every month.
 * -----------------------------------------------------------------
 */
@Component
public class PayslipScheduler {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    /**
     * Scheduled task that runs every month at midnight on the 1st.
     */
    @Scheduled(cron = "0 0 0 1 * *") // 🔹 (sec min hour day month day-of-week)
    public void generateMonthlyPayslips() {
        System.out.println("🧾 Starting automatic payslip generation: " + LocalDate.now());

        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            System.out.println("⚠ No employees found for payslip generation.");
            return;
        }

        String monthYear = LocalDate.now()
                .getMonth()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + " " + LocalDate.now().getYear();

        for (Employee emp : employees) {
            try {
                employeeService.generatePayslip(emp.getId(), monthYear);
                System.out.println("✅ Payslip generated for: " + emp.getFirstName() + " (" + monthYear + ")");
            } catch (Exception e) {
                System.err.println("❌ Error generating payslip for employee ID " + emp.getId());
                e.printStackTrace();
            }
        }

        System.out.println("🎉 All payslips generated successfully for " + monthYear);
    }
}
