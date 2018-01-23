package com.mkath.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mkath.cursomc.services.DBService;
import com.mkath.cursomc.services.EmailService;
import com.mkath.cursomc.services.SmtpEmailService;

@Configuration
@Profile("devpost")
public class DevPostConfig {

	@Autowired
	private DBService dbservice;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDevDataBase() {
		if	("create".equals(strategy)) {
			dbservice.instantiateDataBase();
		}
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
