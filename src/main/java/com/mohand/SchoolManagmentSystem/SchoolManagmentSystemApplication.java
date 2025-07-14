package com.mohand.SchoolManagmentSystem;

import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SchoolManagmentSystemApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(SchoolManagmentSystemApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SchoolManagmentSystemApplication.class);
	}

}
