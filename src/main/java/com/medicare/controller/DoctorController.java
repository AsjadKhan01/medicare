package com.medicare.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.medicare.entities.Doctor;
import com.medicare.entities.Slot;
import com.medicare.repository.DoctorRepo;
import com.medicare.repository.SlotRepo;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
	@Autowired
	private DoctorRepo doctorRepo;
	@Autowired
	private SlotRepo slotRepo;

	@PostMapping("/addSlot")
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
}
