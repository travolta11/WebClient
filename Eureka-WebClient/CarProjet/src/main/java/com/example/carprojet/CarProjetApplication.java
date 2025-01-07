package com.example.carprojet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CarProjetApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarProjetApplication.class, args);
	}



}
