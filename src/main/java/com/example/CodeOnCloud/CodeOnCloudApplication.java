package com.example.CodeOnCloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CodeOnCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeOnCloudApplication.class, args);
	}

}

