package com.mohand.SchoolManagmentSystem;

import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchoolManagmentSystemApplication {

	private static final Logger logger = LoggerFactory.getLogger(SchoolManagmentSystemApplication.class);

	public static void main(String[] args) {
		logger.info("✅ Logging test!");
		logger.info("📁 Working directory: {}", System.getProperty("user.dir"));
		SpringApplication.run(SchoolManagmentSystemApplication.class, args);
	}


}
