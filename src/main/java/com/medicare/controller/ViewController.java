package com.medicare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ViewController {
	@GetMapping("/home")
	public String homePage(Model m) throws NullPointerException {

		m.addAttribute("title", "MediCare +");

		System.out.println("homepage");
		return "home";
	}
	
	@GetMapping("/admin")
	public String admin(Model m) throws NullPointerException {

		m.addAttribute("title", "MediCare Admin");

		System.out.println("MediCare Admin");
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

		m.addAttribute("title", "MediCare Patient");

		System.out.println("MediCare Patient");
		return "patient/patient_panel";
	}

}
