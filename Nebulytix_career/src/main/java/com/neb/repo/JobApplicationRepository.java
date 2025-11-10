package com.neb.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neb.entity.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>{

}
