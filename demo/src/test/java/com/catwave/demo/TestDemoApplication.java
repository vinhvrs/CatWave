package com.catwave.demo;

import org.springframework.boot.SpringApplication;

public class TestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(CatWave::main).with(TestcontainersConfiguration.class).run(args);
	}

}
