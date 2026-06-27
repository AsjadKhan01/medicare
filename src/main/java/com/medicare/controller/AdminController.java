package com.medicare.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicare.Service.DoctorService;
import com.medicare.entities.Doctor;
import com.medicare.repository.DoctorRepo;


@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	DoctorRepo doctorRepo;
	@Autowired
	DoctorService doctorService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@PostMapping("/hire")
	public String hire(@RequestParam("name") String name,@RequestParam("department") String department,
			@RequestParam("exp")int experience,@RequestParam("hospital")String hospital,@RequestParam("email")String email,
			@RequestParam("password")String password , Model model) {
		
		Doctor doctor = new Doctor();
		doctor.setRole("DOCTOR");
		doctor.setStatus("ACTIVE");
		
		doctor.setName(name);
		doctor.setDepartment(department);
		doctor.setExperience(experience);
		doctor.setHospital(hospital);
		doctor.setEmail(email);
		doctor.setPassword(passwordEncoder.encode(password));
				
		this.doctorRepo.save(doctor);
		
		System.err.println(doctor);
		
		return "admin/admin_panel";	}
	
	
}
