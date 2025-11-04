/**
 * --------------------------------------------------------------
 * Purpose :
 *   Data Transfer Object (DTO) for transferring schedule data
 *   between frontend and backend without exposing entity relationships.
 *
 * Description :
 *   - Used in ScheduleController for request and response payloads.
 *   - Keeps API payloads lightweight and avoids circular references.
 * --------------------------------------------------------------
 */

package com.neb.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ScheduleDTO {

    private Long id;             // Schedule ID
    private String title;        // Title of the schedule
    private String description;  // Description of the event
    private LocalDateTime startTime; // Start time
    private LocalDateTime endTime;   // End time
    private String location;     // Meeting location or link
    private String status;       // UPCOMING / COMPLETED / CANCELLED

    private Long createdById;    // Admin or HR ID who created it
    private Long employeeId;     // Employee ID assigned to the schedule
}
