package com.neb.service;

import com.neb.dto.AddJobApplicationRequestDto;
import com.neb.dto.AddJobApplicationResponseDto;
import com.neb.dto.JobDetailsDto;
import com.neb.dto.OtpVerificationRequestDto;

public interface CareerPageService {

	public JobDetailsDto getJobById(Long id);
	 AddJobApplicationResponseDto applyForJob(AddJobApplicationRequestDto requestDto);
	  String verifyOtp(OtpVerificationRequestDto request);
}
