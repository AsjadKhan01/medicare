package com.medicare.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicare.entities.Doctor;
import com.medicare.repository.DoctorRepo;

@Controller
public class ViewController {
	@Autowired
	private DoctorRepo doctorRepo;

	@GetMapping("/home")
	public String homePage(Model m) throws NullPointerException {

		m.addAttribute("title", "MediCare +");

		System.out.println("homepage");
		return "home";
	}

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

	@GetMapping("/doctor")
	public String doctor(Model m) throws NullPointerException {

		m.addAttribute("title", "MediCare Doctor");

		System.out.println("MediCare Doctor");
		return "doctor/doctor_panel";
	}

	@GetMapping("/patient")
	public String patient(Model m) throws NullPointerException {
		List<Doctor> doctors = doctorRepo.findAll();
		m.addAttribute("doctors", doctors);

		m.addAttribute("title", "MediCare Patient");

		System.out.println("MediCare Patient");
		return "patient/patient_panel";
	}

}
