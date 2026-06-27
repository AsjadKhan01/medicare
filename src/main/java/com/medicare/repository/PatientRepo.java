package com.medicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.entities.Patient;

public interface PatientRepo extends JpaRepository<Patient, Integer>{

	Patient findByEmail(String email);
	


}
