package com.neb.dto;

import java.time.LocalDate;

import com.neb.constants.WorkStatus;

import lombok.Data;

/**
 * ---------------------------------------------------------------
 * File Name   : AddWorkRequestDto.java
 * Package     : com.neb.dto
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used to collect and transfer work assignment details
 *   between the frontend and backend when a new task is assigned
 *   to an employee.
 *
 * Description :
 *   - It acts as a Data Transfer Object (DTO) that carries work-related data.
 *   - It contains information about the task title, description, assigned
 *     and due dates, current status, and submission details.
 *   - It also links the work item to a specific employee through the
 *     employeeId field.
 *
 * Fields :
 *   title, description        → Basic information about the work/task
 *   assignedDate, dueDate     → Task scheduling details
 *   status                    → Current status of the task (e.g., ASSIGNED, COMPLETED)
 *   reportDetails             → Work progress or completion report provided by employee
 *   submittedDate             → Date when the work was submitted
 *   employeeId                → ID of the employee to whom the work is assigned
 *
 * Result :
 *   This DTO simplifies data exchange between the frontend and backend
 *   for assigning, tracking, and submitting work tasks.
 * ---------------------------------------------------------------
 */

@Data
public class AddWorkRequestDto {
    private String title;
    private String description;
    private LocalDate dueDate;
    private Long employeeId;
}
