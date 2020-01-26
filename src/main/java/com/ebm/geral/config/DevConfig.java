package com.ebm.geral.config;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import com.ebm.geral.service.PopulaBD;


@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private PopulaBD dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	@Transactional
	@Bean
	public boolean intantiateDatabase() throws ParseException {
		if(isCreateStrategy())
			dbService.populaBD();
		return true;
	}
	
	public boolean isCreateStrategy() {
		return this.strategy.toLowerCase().equals("create");
	}
}
