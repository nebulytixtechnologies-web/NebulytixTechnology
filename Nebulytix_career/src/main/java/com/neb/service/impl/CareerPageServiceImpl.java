package com.neb.service.impl;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neb.dto.JobDetailsDto;
import com.neb.entity.Job;
import com.neb.exception.CustomeException;
import com.neb.repo.JobApplicationRepository;
import com.neb.repo.JobRepository;
import com.neb.service.CareerPageService;

@Service
public class CareerPageServiceImpl implements CareerPageService{

	@Autowired
	private JobApplicationRepository jobApplicationRepository;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
    private ModelMapper mapper;

	@Override
	public JobDetailsDto getJobById(Long id) {
		
		Job job = jobRepository.findById(id).orElseThrow(()->new CustomeException("job not found with id:"+id));
		
		LocalDate today = LocalDate.now();
		 if (job.getClosingDate() != null && job.getClosingDate().isBefore(today)) {
                job.setIsActive(false);
            } else {
                job.setIsActive(true);
            }
		 
		JobDetailsDto jobDetailsRes = mapper.map(job, JobDetailsDto.class);
		
		return jobDetailsRes;
	}
}
