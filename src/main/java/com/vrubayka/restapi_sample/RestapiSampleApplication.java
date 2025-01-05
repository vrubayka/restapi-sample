package com.vrubayka.restapi_sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.vrubayka.restapi_sample")
public class RestApiSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiSampleApplication.class, args);
	}

}
