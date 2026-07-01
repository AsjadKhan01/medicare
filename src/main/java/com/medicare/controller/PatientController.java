package com.medicare.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.standard.expression.Each;

import com.medicare.entities.Appointment;
import com.medicare.entities.Doctor;
import com.medicare.entities.Patient;
import com.medicare.entities.Slot;
import com.medicare.repository.AppointmentRepo;
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
	@Autowired
	private AppointmentRepo appointmentRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
//	private Patient patient;
	Principal principal;

	@GetMapping("/")
	public String base(Principal principal, Model m) {

		m.addAttribute("loginUsername", this.patientRepo.findByEmail(principal.getName()).getName());
		System.err.println("provoke///////////");

		return "redirect:/patient/findMyDoctor";
	}

	@PostMapping("/bookSlot")
	public String bookSlot(@RequestParam("doctorId") int doctorId, @RequestParam("slotId") int slotId,
			@RequestParam("reason") String reason, Model m, Principal principal) {
		String name = principal.getName();

		Patient patient = this.patientRepo.findByEmail(name);
		Doctor doctor = this.doctorRepo.findById(doctorId);
		Slot slot = this.slotRepo.findById(slotId);
		if (slot == null || !slot.getStatus().equals("AVAILABLE")) {
			return "redirect:/patient/findMyDoctor?error=slotUnavailable";
		}
		//slot ka status PENDING karna hai yha
		slot.setStatus("PENDING");
		Appointment appointment = new Appointment();
		appointment.setPatient(patient);
		appointment.setDoctor(doctor);
		appointment.setSlot(slot);
		appointment.setReason(reason);
		appointment.setStatus("PENDING");
		appointment.setAction("NON-COMPLETE");
		this.appointmentRepo.save(appointment);

		return "redirect:/patient/findMyDoctor";
	}

	@GetMapping("/findMyDoctor")
	public String findDoctor(Model m, Principal principal) throws NullPointerException {
		List<Doctor> doctors = doctorRepo.findAll();

		// Har doctor ke sirf upcoming + available slots filter karna
		for (Doctor doctor : doctors) {
			List<Slot> filteredSlots = slotRepo
					.findByDoctorIdAndDateGreaterThanEqualOrderByDateAscTimeAsc(doctor.getId(), LocalDate.now());
			filteredSlots.removeIf(slot -> !slot.getStatus().equals("AVAILABLE"));
			doctor.setSlots(filteredSlots); // doctor object ke slots ko replace kar diya
		}

		m.addAttribute("doctors", doctors);
		m.addAttribute("activePage", "findMyDoctor");
		m.addAttribute("title", "MediCare Patient");
		m.addAttribute("loginUsername", this.patientRepo.findByEmail(principal.getName()).getName());

		return "/patient/findMyDoctor";
	}

	@GetMapping("/appointment")
	public String appointment(Model m, Principal principal) {

		// Sabse pehle mujhe Doctor ki id chahiye

		String name = principal.getName();
		Patient patient = this.patientRepo.findByEmail(name);

		List<Appointment> appointments = this.appointmentRepo.findByPatient(patient);

		System.err.println("patientId : ");
		m.addAttribute("appointments", appointments);
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
