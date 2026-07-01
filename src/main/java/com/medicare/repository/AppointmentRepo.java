package com.medicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.entities.Appointment;
import com.medicare.entities.Doctor;
import com.medicare.entities.Patient;

import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {

	List<Appointment> findByPatient(Patient patient);

	List<Appointment> findByDoctor(Doctor doctor);

	List<Appointment> findByDoctorAndStatus(Doctor doctor, String status);

	long countByStatus(String status);

	Appointment findById(int appointmentId);
}
