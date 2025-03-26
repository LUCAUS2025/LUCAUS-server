package com.likelion13.lucaus_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LucausApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LucausApiApplication.class, args);
	}

}
