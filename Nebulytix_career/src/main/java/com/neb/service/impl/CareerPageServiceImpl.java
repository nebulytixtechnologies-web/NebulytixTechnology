package com.neb.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neb.dto.AddJobApplicationRequestDto;
import com.neb.dto.AddJobApplicationResponseDto;
import com.neb.dto.JobDetailsDto;
import com.neb.dto.OtpVerificationRequestDto;
import com.neb.entity.Job;
import com.neb.entity.JobApplication;
import com.neb.exception.CustomeException;
import com.neb.repo.JobApplicationRepository;
import com.neb.repo.JobRepository;
import com.neb.service.CareerPageService;
import com.neb.service.EmailService;

@Service
public class CareerPageServiceImpl implements CareerPageService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EmailService emailService;

    // Temporary OTP storage
    private final Map<String, String> otpStore = new HashMap<>();

    /**
     * ✅ Get job details by ID
     */
    @Override
    public JobDetailsDto getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new CustomeException("Job not found with id: " + id));

        LocalDate today = LocalDate.now();
        job.setIsActive(job.getClosingDate() == null || !job.getClosingDate().isBefore(today));

        return mapper.map(job, JobDetailsDto.class);
    }

    /**
     * ✅ Step 1: Apply for a job — send OTP to email
     */
    @Override
    public AddJobApplicationResponseDto applyForJob(AddJobApplicationRequestDto requestDto) {
        Job job = jobRepository.findById(requestDto.getJobId())
                .orElseThrow(() -> new CustomeException("Job not found with ID: " + requestDto.getJobId()));

        // Check if user already applied (same email or same domain)
        boolean exists = jobApplicationRepository.findAll().stream()
                .anyMatch(app -> app.getEmail().equalsIgnoreCase(requestDto.getEmail()));

        if (exists) {
            throw new CustomeException("You already applied for a job with this email!");
        }

        // Generate OTP and store temporarily
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStore.put(requestDto.getEmail(), otp);

        // Send OTP mail
        emailService.sendApplicationMail(
                requestDto.getEmail(),
                "Verify Your Email - Job Application",
                "Dear " + requestDto.getFullName() + ",\n\n" +
                        "Your OTP for job application verification is: " + otp +
                        "\n\nPlease use this OTP to verify your application.\n\nThank you!"
        );

        // Response
        AddJobApplicationResponseDto response = new AddJobApplicationResponseDto();
        response.setFullName(requestDto.getFullName());
        response.setEmail(requestDto.getEmail());
        response.setPhoneNumber(requestDto.getPhoneNumber());
        response.setLinkedinUrl(requestDto.getLinkedinUrl());
        response.setResumeFileName(requestDto.getResumeFileName());
        response.setApplicationDate(LocalDateTime.now());
        response.setStatus("OTP_SENT");

        return response;
    }

    /**
     * ✅ Step 2: Verify OTP and save job application
     */
    @Override
    public String verifyOtp(OtpVerificationRequestDto request) {
        String storedOtp = otpStore.get(request.getEmail());

        if (storedOtp == null || !storedOtp.equals(request.getOtp())) {
            throw new CustomeException("Invalid or expired OTP!");
        }

        // Remove OTP after verification
        otpStore.remove(request.getEmail());

        // Fetch job
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new CustomeException("Job not found with ID: " + request.getJobId()));

        // Save application
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setFullName(request.getFullName());
        application.setEmail(request.getEmail());
        application.setPhoneNumber(request.getPhoneNumber());
        application.setLinkedinUrl(request.getLinkedinUrl());
        application.setResumeFilePath(request.getResumeFilePath());
        application.setResumeFileName(request.getResumeFileName());
        application.setApplicationDate(LocalDateTime.now());
        application.setStatus("SUBMITTED");

        jobApplicationRepository.save(application);

        // Send confirmation email
        emailService.sendApplicationMail(
                request.getEmail(),
                "Job Application Submitted Successfully",
                "Dear " + request.getFullName() + ",\n\n" +
                        "Your job application has been successfully submitted. " +
                        "Our team will review your profile and get back to you soon.\n\nThank you!"
        );

        return "Application submitted successfully! Confirmation email sent.";
    }
}
