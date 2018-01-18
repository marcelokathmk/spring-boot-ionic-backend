package com.mkath.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mkath.cursomc.services.DBService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DBService dbservice;
	
	@Bean
	public boolean instantiateDataBase() {
		dbservice.instantiateDataBase();
		return true;
	}
}
