package com.medicare.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.medicare.repository.PatientRepo;

@Controller
public class AuthController {
@Autowired
private PatientRepo patientRepo;
    @GetMapping("/login")
    public String loginPage(Model m, Principal principal) throws NullPointerException {
		m.addAttribute("activePage", "login");
		if(principal != null) {
		m.addAttribute("loginUsername", this.patientRepo.findByEmail(principal.getName()).getName());	
		}

        return "login"; // templates/login.html
    }
}
