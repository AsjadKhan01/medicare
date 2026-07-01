package com.medicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.entities.Doctor;
import java.util.List;


public interface DoctorRepo extends JpaRepository<Doctor, Integer>{

	long countByStatus(String status);

	Doctor findByEmail(String email);
	
	Doctor findById(int id);

}
