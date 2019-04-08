package com.ebm.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ebm.pessoal.service.PessoaPopulaTest;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private PessoaPopulaTest dbService;
	 
	@Bean
	public boolean intantiateDatabase() throws ParseException {
		//dbService.insert();
		return true;
	}
	
	
}
