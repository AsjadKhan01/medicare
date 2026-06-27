package com.medicare.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.medicare.entities.Admin;
import com.medicare.entities.Doctor;
import com.medicare.entities.Patient;
import com.medicare.repository.AdminRepo;
import com.medicare.repository.DoctorRepo;
import com.medicare.repository.PatientRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PatientRepo patientRepo;
    
    @Autowired
    private AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    	//pehle admin check kro
    	Admin admin = adminRepo.findByEmail(email);
    	if(admin != null) {
    		return new CustomUserDetails(admin.getEmail(), admin.getPassword(), admin.getRole());
    	}
    	
        //Doctor table mein check kar
        Doctor doctor = doctorRepo.findByEmail(email);
        if (doctor != null) {
            return new CustomUserDetails(doctor.getEmail(), doctor.getPassword(), doctor.getRole());
        }

        // Doctor mein nahi mila, toh Patient table mein check kar
        Patient patient = patientRepo.findByEmail(email);
        if (patient != null) {
            return new CustomUserDetails(patient.getEmail(), patient.getPassword(), patient.getRole());
        }

        // Kahi nahi mila
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}