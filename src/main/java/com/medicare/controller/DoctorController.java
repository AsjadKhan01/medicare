package com.medicare.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.medicare.entities.Appointment;
import com.medicare.entities.Doctor;
import com.medicare.entities.Slot;
import com.medicare.repository.AppointmentRepo;
import com.medicare.repository.DoctorRepo;
import com.medicare.repository.SlotRepo;

@Controller
public class DoctorController {
	@Autowired
	private DoctorRepo doctorRepo;
	@Autowired
	private SlotRepo slotRepo;
	@Autowired
	private AppointmentRepo appointmentRepo;

	@GetMapping("/doctor")
	public String doctor(Model m, Principal principal) throws NullPointerException {
		String name = principal.getName() ;
		int doctorId = this.doctorRepo.findByEmail(name).getId();
		List<Slot> slots = this.slotRepo.findByDoctorIdAndDateGreaterThanEqualOrderByDateAscTimeAsc( doctorId, LocalDate.now());
		m.addAttribute("title", "MediCare Doctor");
		m.addAttribute("slots", slots);
		
		//Pending Request ke liye...
		//doctor ki ligin id get kro  || Login Doctor ki email lakar dega
		String email = principal.getName();
		Doctor doctor = this.doctorRepo.findByEmail(email);

		//ab login doctor ki sari pending appointment chahiye
		List<Appointment> appointments = this.appointmentRepo.findByDoctorAndStatus(doctor, "PENDING");
		List<Appointment> confermAppointment = this.appointmentRepo.findByDoctorAndStatus(doctor, "CONFIRM");

		m.addAttribute("appointments", appointments);
		m.addAttribute("confermAppointment", confermAppointment);
		//pending request number | todays appointment number | Seen patient number
		long pending = this.appointmentRepo.countByStatus("PENDING");
		long confirm = this.appointmentRepo.countByStatus("CONFIRM");
		long seen = this.appointmentRepo.countByStatus("SEEN");

		m.addAttribute("pendingCount", pending);
		m.addAttribute("confirmCount", confirm);
		m.addAttribute("seenCount", seen);
		System.out.println("MediCare Doctor" + seen);
		return "doctor/doctor_panel";
	}
	@PostMapping("/doctor/addSlot")
	public String addSlot(@RequestParam("date") String date, @RequestParam("time") String time, Principal principal) {

		String doctorEmail = principal.getName(); // logged-in doctor ka email
		Doctor doctor = doctorRepo.findByEmail(doctorEmail);

		Slot slot = new Slot();
		slot.setDate(LocalDate.parse(date));
		slot.setTime(LocalTime.parse(time));
		slot.setStatus("AVAILABLE");
		slot.setDoctor(doctor);

		slotRepo.save(slot);

	    return "redirect:/doctor"; // apna actual dashboard URL daal
	}
	@PostMapping("/docPendingReq")
	public String DoctorRequestPending(@RequestParam("action") String action , @RequestParam("appointmentId") int appointmentId) {
		
		Appointment appointment = this.appointmentRepo.findById(appointmentId);
		
		if(action.equals("accept")) {
			appointment.setStatus("CONFIRM");
		}else if(action.equals("reject")){
			appointment.setStatus("REJECT");
		}
		
		appointmentRepo.save(appointment);
		System.err.println(action +" : "+appointmentId);
		return "redirect:/doctor";
	}
	@PostMapping("/confirmQue")
	public String confirmQueue(@RequestParam("action") String action, @RequestParam("appointmentId") int appointmentId) {
		
		Appointment appointment = this.appointmentRepo.findById(appointmentId);
		System.err.println(appointment+ " >>><<< "+ action);
		appointment.setStatus("SEEN");
		appointmentRepo.save(appointment);
		return "redirect:/doctor";
	}
}
