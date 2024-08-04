package com.BEM.Data.MongoDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class DataMongoDbApplication {

 public static void main(String[] args) {
		SpringApplication.run(DataMongoDbApplication.class, args);
	}

}
