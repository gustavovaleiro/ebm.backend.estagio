package com.ebm.geral.config;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.geral.service.PopulaBD;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private PopulaBD dbService;
	 
	@Transactional
	@Bean
	public boolean intantiateDatabase() throws ParseException {
		//dbService.populaBD();
		return true;
	}
	
	
}
