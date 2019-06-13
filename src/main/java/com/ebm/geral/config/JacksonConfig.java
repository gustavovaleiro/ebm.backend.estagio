package com.ebm.geral.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.Servico;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Configuration
public class JacksonConfig {

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
			public void configure(ObjectMapper objectMapper) {
				objectMapper.registerSubtypes(PessoaFisica.class);
				objectMapper.registerSubtypes(PessoaJuridica.class);
				objectMapper.registerSubtypes(Produto.class);
				objectMapper.registerSubtypes(Servico.class);
				 objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
				
				super.configure(objectMapper);
			};
		};
		return builder.modulesToInstall(new JavaTimeModule());
	}

}
