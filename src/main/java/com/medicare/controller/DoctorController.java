package com.medicare.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicare.entities.Doctor;

@Controller
public class DoctorController {

	ArrayList<Doctor> list = new ArrayList<>();

	@PostMapping("/admin/hire")
	public String hireDoctor(@RequestParam("name") String name,@RequestParam("department") String department,
			@RequestParam("exp")int experience,@RequestParam("hospital")String hospital,@RequestParam("email")String email,
			@RequestParam("password")String password) {
		
		Doctor doctor = new Doctor();
		doctor.setId(101);
		doctor.setRole("DOCTOR");
		
		doctor.setName(name);
		doctor.setDepartment(department);
		doctor.setExperience(experience);
		doctor.setHospital(hospital);
		doctor.setEmail(email);
		doctor.setPassword(password);
				
		list.add(doctor);
		System.out.println(list);
		System.err.println(">");
		
		return "ok" ;
	}
	
//	@GetMapping("/show")
//	public ArrayList<Doctor> show(){
//		return list;
//	}
	
}
