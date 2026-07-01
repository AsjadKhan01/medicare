package com.medicare.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicare.entities.Doctor;
import com.medicare.entities.Slot;
import com.medicare.repository.DoctorRepo;
import com.medicare.repository.SlotRepo;

@Controller
public class ViewController {
	@Autowired
	private DoctorRepo doctorRepo;
	
	@Autowired
	private SlotRepo slotRepo;

	@GetMapping("/home")
	public String homePage(Model m) throws NullPointerException {

		m.addAttribute("title", "MediCare +");

		System.out.println("homepage");
		return "home";
	}


}
