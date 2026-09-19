package com.example.prabhim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PrabhimApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrabhimApplication.class, args);
	}

}
