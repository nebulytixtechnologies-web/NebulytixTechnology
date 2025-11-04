package com.neb.dto;

import java.time.LocalDate;
import com.neb.constants.WorkStatus;
import lombok.Data;
/**
 * ---------------------------------------------------------------
 * File Name   : WorkResponseDto.java
 * Package     : com.neb.dto
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used to send detailed work or task information
 *   from the backend to the frontend after being assigned or fetched.
 *
 * Description :
 *   - It acts as a Data Transfer Object (DTO) for representing
 *     work-related details assigned to employees.
 *   - It contains both task-specific information and the details
 *     of the employee responsible for the task.
 *
 * Fields :
 *   id                 → Unique identifier of the work/task
 *   title, description → Task title and description details
 *   assignedDate       → Date when the work was assigned
 *   dueDate            → Deadline for task completion
 *   status             → Current work status (e.g., ASSIGNED, COMPLETED)
 *   reportDetails      → Work report or progress summary
 *   submittedDate      → Date when the work was submitted
 *   attachmentUrl      → Optional file or document link for submission
 *   employeeId         → ID of the employee assigned to the work
 *   employeeName       → Full name of the employee
 *   employeeEmail      → Email address of the employee
 *
 * Result :
 *   This DTO helps in sending a clear and structured response
 *   of employee work assignments and progress details to the frontend.
 * ---------------------------------------------------------------
 */

@Data
public class WorkResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate assignedDate;
    private LocalDate dueDate;
    private WorkStatus status;
    private String reportDetails;
    private LocalDate submittedDate;
    private String attachmentUrl;
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
}
