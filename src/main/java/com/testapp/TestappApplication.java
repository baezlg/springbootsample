package com.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = "com.*")
public class TestappApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestappApplication.class, args);
	}

}
