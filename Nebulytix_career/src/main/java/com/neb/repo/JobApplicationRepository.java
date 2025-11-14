package com.neb.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neb.entity.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>{

	boolean existsByEmailIgnoreCase(String email);
	Optional<JobApplication> findByEmailIgnoreCase(String email);
}
