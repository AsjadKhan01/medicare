package com.medicare.entities;

import java.util.ArrayList;

public class Doctor {

	private int id;
	private String name;
	private String department;
	private int experience;
	private String hospital;
	private String email;
	private String password;
	private String role;
	private String about;
	private String status;
	private ArrayList<String> slots;
	private Patient patients;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<String> getSlots() {
		return slots;
	}
	public void setSlots(ArrayList<String> slots) {
		this.slots = slots;
	}
	public Patient getPatients() {
		return patients;
	}
	public void setPatients(Patient patients) {
		this.patients = patients;
	}
	@Override
	public String toString() {
		return "Doctor [id=" + id + ", name=" + name + ", department=" + department + ", experience=" + experience
				+ ", hospital=" + hospital + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", about=" + about + ", status=" + status + ", slots=" + slots + ", patients=" + patients + "]";
	}
	
	
}
