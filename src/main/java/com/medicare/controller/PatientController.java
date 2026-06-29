package com.medicare.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.standard.expression.Each;

import com.medicare.entities.Doctor;
import com.medicare.entities.Patient;
import com.medicare.entities.Slot;
import com.medicare.repository.DoctorRepo;
import com.medicare.repository.PatientRepo;
import com.medicare.repository.SlotRepo;

@Controller
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private DoctorRepo doctorRepo;
	@Autowired
	private PatientRepo patientRepo;
	@Autowired
	private SlotRepo slotRepo;
	private Patient patient;
	@Autowired
	private PasswordEncoder passwordEncoder;

	Principal principal;

	@GetMapping("/")
	public String base(Principal principal, Model m) {

		m.addAttribute("loginUsername", this.patientRepo.findByEmail(principal.getName()).getName());
		System.err.println("provoke///////////");

		return "redirect:/patient/findMyDoctor";
	}

	@GetMapping("/findMyDoctor")
	public String findDoctor(Model m, Principal principal) throws NullPointerException {
		List<Doctor> doctors = doctorRepo.findAll();

		List<Doctor> findAllDoctors = this.doctorRepo.findAll();
		// Har doctor ke available slots ko date-wise group karna
		Map<Integer, List<Slot>> doctorSlotMap = new HashMap<>();
		for (Doctor doctor : findAllDoctors) {
			List<Slot> availableSlots = this.slotRepo
					.findByDoctorIdAndDateGreaterThanEqualOrderByDateAscTimeAsc(doctor.getId(), LocalDate.now());
			// availableSlots.removeIf(slot -> !slot.getStatus().equals("AVAILABLE"));
			doctorSlotMap.put(doctor.getId(), availableSlots);
		}
		//m.addAttribute("doctorSlotMap", doctorSlotMap);
		m.addAttribute("loginUsername", this.patientRepo.findByEmail(principal.getName()).getName());
		//m.addAttribute("doctor", this.doctorRepo.findByEmail(principal.getName()));
		m.addAttribute("doctors", doctors);
		m.addAttribute("activePage", "findMyDoctor");
		m.addAttribute("title", "MediCare Patient");

		System.out.println("MediCare Patient");
		return "/patient/findMyDoctor";
	}

	@GetMapping("/appointment")
	public String appointment(Model m, Principal principal) {
		m.addAttribute("title", "appointment");
		m.addAttribute("activePage", "appointment");
		m.addAttribute("loginUsername", this.patientRepo.findByEmail(principal.getName()).getName());

		return "/patient/appointment";
	}

	@GetMapping("/signUp")
	public String signUp(Model m, Principal principal) {
		m.addAttribute("title", "Please SignUp");
		m.addAttribute("activePage", "signUp");
		m.addAttribute("loginUsername", this.patientRepo.findByEmail(principal.getName()).getName());

		return "/patient/signUp";
	}

	@PostMapping("/register")
	public String patientRegister(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, Model m, Principal principal) {

		// Check email already exist to nhi karta
		if (patientRepo.findByEmail(email) != null) {
			m.addAttribute("error", "Email already registered. Try logging in.");
		}

		Patient patient = new Patient();
		patient.setName(name);
		patient.setEmail(email);
		patient.setPassword(this.passwordEncoder.encode(password));
		patient.setRole("PATIENT");

		this.patientRepo.save(patient);

		return "/patient/signUp";
	}
}
