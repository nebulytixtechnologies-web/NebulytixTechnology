package com.neb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neb.dto.JobDetailsDto;
import com.neb.dto.ResponseMessage;
import com.neb.service.CareerPageService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/career")
public class CareerPageController {

	@Autowired
	private CareerPageService service;
	
	@GetMapping("/job/{id}")
	public ResponseEntity<ResponseMessage<JobDetailsDto>> getJobById(@PathVariable("id") Long id) {
	    JobDetailsDto job = service.getJobById(id);
	    if (job == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(new ResponseMessage<JobDetailsDto>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), "Job not found"));
	    }
	    return ResponseEntity.ok(new ResponseMessage<JobDetailsDto>(HttpStatus.OK.value(), HttpStatus.OK.name(), "job fetched successfully", job));
	}
}
