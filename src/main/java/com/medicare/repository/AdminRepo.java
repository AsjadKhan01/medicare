package com.medicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.entities.Admin;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);
}