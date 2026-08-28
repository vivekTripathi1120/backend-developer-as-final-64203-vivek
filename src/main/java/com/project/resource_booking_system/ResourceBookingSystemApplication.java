package com.project.resource_booking_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ResourceBookingSystemApplication {

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void printPassword(){
		System.out.println(passwordEncoder().encode("password"));
		System.out.println(passwordEncoder().encode("1234"));
	}

	public static void main(String[] args) {
		printPassword();
		SpringApplication.run(ResourceBookingSystemApplication.class, args);


	}

}
