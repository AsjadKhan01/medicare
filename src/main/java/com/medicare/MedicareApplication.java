package com.medicare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.medicare.entities.Admin;
import com.medicare.repository.AdminRepo;

@SpringBootApplication
public class MedicareApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicareApplication.class, args);
	System.err.println("Medicare+");
	}

	@Bean
	public CommandLineRunner createDefaultAdmin(AdminRepo adminRepo, PasswordEncoder passwordEncoder) {
		return args -> {
			if (adminRepo.findByEmail("admin@medicare.com") == null) {
				Admin admin = new Admin();
				admin.setName("Super Admin");
				admin.setEmail("admin@medicare.com");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole("ADMIN");
				adminRepo.save(admin);
				System.out.println("Default admin created: admin@medicare.com / admin123");
			}
		};
	}
}