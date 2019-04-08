//package com.ebm.pessoal.resource;
//
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.client.RestTemplate;
//
//import com.ebm.pessoal.domain.Pessoa;
//import com.ebm.pessoal.domain.PessoaFisica;
//import com.ebm.pessoal.domain.PessoaJuridica;
//import com.ebm.pessoal.service.PessoaPopulaTest;
//import com.ebm.pessoal.service.PessoaService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//public class PessoaEndPointTest {
//	
//	@Value("${server.port}")
//	private String port;
//	
//	private  String basePath;
//	
//	@Autowired
//	private PessoaService pessoaService;
//	@Autowired
//	private PessoaPopulaTest carga;
//	
//	private RestTemplate restTemplate;
//
//	private ObjectMapper MAPPER = new ObjectMapper();
//	
//	
//	@Before
//	public void setUp() throws Exception {
//		pessoaService.deleteAll(true);
//		basePath = "http://localhost:" + port + "/pessoas";
//		carga.InstanciaPessoas();
//		pessoaService.insert(carga.getPf1());
//		pessoaService.insert(carga.getPf2());
//		pessoaService.insert(carga.getPj1());
//		pessoaService.insert(carga.getPj2());
//		
//		restTemplate = new RestTemplate();
//		
//	}
//
//	@Test
//	public void testInsertPerson() {
//		
//			PessoaFisica pf = carga.getPf3();
//			PessoaJuridica pj = carga.getPj3();
//			
//			
//			 ResponseEntity pf1 = restTemplate.postForObject(basePath, pf, ResponseEntity.class);
//			ResponseEntity pj1  = restTemplate.postForObject(basePath, pj, ResponseEntity.class);
//		
//		
//			
//		//	Assert.assertEquals(pf.getNome(), pf1.cr;
//	}
//	
//	
//}
