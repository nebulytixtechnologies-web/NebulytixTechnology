/**
 * ---------------------------------------------------------------
 * File Name   : WorkStatus.java
 * Package     : com.neb.constants
 * ---------------------------------------------------------------
 * Purpose :
 *   This enum is used to define different stages of an employee's work task.
 *
 * Description :
 *   - ASSIGNED     → Task is given by the Admin.
 *   - IN_PROGRESS  → Employee has started working on the task.
 *   - COMPLETED    → Employee has finished the work but not submitted the report yet.
 *   - REPORTED     → Employee has submitted the final report.
 *
 * Result :
 *   Helps in tracking and managing the current status of each task easily.
 * ---------------------------------------------------------------
 */

package com.neb.constants;

public enum WorkStatus {
    ASSIGNED,      // Task given by Admin
    IN_PROGRESS,   // Work started by Employee
    COMPLETED,     // Work finished but not yet reported
    REPORTED       // Final report submitted
}
