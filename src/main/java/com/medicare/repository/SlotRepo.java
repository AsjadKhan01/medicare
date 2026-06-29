package com.medicare.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.entities.Slot;

public interface SlotRepo extends JpaRepository<Slot, Integer> {
    List<Slot> findByDoctorId(int doctorId);
    
    List<Slot> findByDoctorIdAndDateGreaterThanEqualOrderByDateAscTimeAsc(int doctorId, LocalDate date);
}