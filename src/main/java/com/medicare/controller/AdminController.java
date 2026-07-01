package com.medicare.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medicare.Service.DoctorService;
import com.medicare.entities.Doctor;
import com.medicare.repository.DoctorRepo;


@Controller
public class AdminController {

	@Autowired
	DoctorRepo doctorRepo;
	@Autowired
	DoctorService doctorService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/admin")
	public String admin(Model m) throws NullPointerException {

		long activeCount = doctorRepo.countByStatus("ACTIVE");
		m.addAttribute("activeCount", activeCount);

		m.addAttribute("title", "MediCare Admin");
		List<Doctor> doctors = doctorRepo.findAll();
		m.addAttribute("doctors", doctors);
		System.out.println("MediCare Admin");

		// Group doctors by department and count
		Map<String, Long> deptCounts = doctors.stream()
				.collect(Collectors.groupingBy(Doctor::getDepartment, Collectors.counting()));
		m.addAttribute("deptCounts", deptCounts);

		return "admin/admin_panel";
	}
	
	@PostMapping("/admin/hire")
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
