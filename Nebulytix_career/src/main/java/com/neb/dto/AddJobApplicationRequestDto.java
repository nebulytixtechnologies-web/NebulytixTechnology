package com.neb.dto;



import lombok.Data;

@Data
public class AddJobApplicationRequestDto {
    private Long jobId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String linkedinUrl;
    private String resumeFilePath;
    private String resumeFileName;
}

