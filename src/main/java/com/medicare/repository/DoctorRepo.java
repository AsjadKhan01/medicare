package com.medicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.entities.Doctor;

public interface DoctorRepo extends JpaRepository<Doctor, Integer>{

	long countByStatus(String status);

	Doctor findByEmail(String email);

}
