package dev.cgomezu.springboot.microservices.ratingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.cgomezu.springboot.microservices.ratingservice")
public class RatingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingserviceApplication.class, args);
	}
}
