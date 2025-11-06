package com.neb.service.impl;

/**
 * --------------------------------------------------------------
 * Purpose :
 *   Implements business logic for managing schedules.
 *
 * Description :
 *   - Handles CRUD operations for the Schedule entity.
 *   - Interacts with the ScheduleRepository to perform database actions.
 *   - Includes exception handling for missing employee schedules.
 * --------------------------------------------------------------
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neb.entity.Schedule;
import com.neb.exception.CustomeException;  // ✅ Add this import
import com.neb.repo.ScheduleRepository;
import com.neb.service.ScheduleService;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    // ✅ Create or update a schedule
    @Override
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // ✅ Get all schedules
    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // ✅ Get schedules by employee ID (with exception handling)
    @Override
    public List<Schedule> getSchedulesByEmployee(Long employeeId) {
        List<Schedule> schedules = scheduleRepository.findByEmployeeId(employeeId);
        if (schedules.isEmpty()) {
            // 🔹 Throw custom exception if no schedules are found
            throw new CustomeException("No schedules found for employee ID: " + employeeId);
        }
        return schedules;
    }

    // ✅ Delete a schedule by ID
    @Override
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
