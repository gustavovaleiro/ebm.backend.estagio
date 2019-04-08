//package dev.gustavovalerio;
//
//import org.mockito.Mockito;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//
//import com.ebm.pessoal.repository.PessoaFisicaRepository;
//import com.ebm.pessoal.repository.PessoaJuridicaRepository;
//
//@Profile("test")
//@Configuration
//public class BeansConfiguration {
//	
//	@Bean
//	@Primary
//	public PessoaFisicaRepository pessoaFisicaRepository() {
//		return Mockito.mock(PessoaFisicaRepository.class);
//	}
//	
//	@Bean
//	@Primary
//	public PessoaJuridicaRepository pessoaJuridicaRepository() {
//		return Mockito.mock(PessoaJuridicaRepository.class);
//	}
//	
//	
//	
//}
