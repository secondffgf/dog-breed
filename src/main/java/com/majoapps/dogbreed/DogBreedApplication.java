package com.majoapps.dogbreed;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EnableJpaAuditing
public class DogBreedApplication {

	public static void main(String[] args) {
		SpringApplication.run(DogBreedApplication.class, args);
	}

}
