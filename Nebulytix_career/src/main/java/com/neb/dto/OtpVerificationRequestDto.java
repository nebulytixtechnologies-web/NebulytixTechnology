package com.neb.dto;

import lombok.Data;

@Data
public class OtpVerificationRequestDto {
    private String email;
    private String otp;
    private Long jobId;
    private String fullName;
    private String phoneNumber;
    private String linkedinUrl;
    private String resumeFilePath;
    private String resumeFileName;
}
