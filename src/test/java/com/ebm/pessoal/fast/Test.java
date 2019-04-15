package com.ebm.pessoal.fast;



import static org.assertj.core.api.Assertions.doesNotHave;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class Test {
	
	private CargoT recrutador;
	private CargoT desenvolvedor;
	private PessoaT pf1;
	private PessoaT pf2;
	private PessoaT pj1;
	private PessoaT pj2;
	private FuncionarioT f1;
	private FuncionarioT f2;
	private FuncionarioT f3;
	private FuncionarioT f4;
	
	@Autowired
	CargoTRepository cargos;
	@Autowired
	FuncionarioTRepository funcionarios;
	@Autowired
	PessoaTRepository pessoas;
	 @PersistenceContext
	private EntityManager entity;

	@Before

	public void instanciaObjetos() {
		funcionarios.deleteAll();
		cargos.deleteAll();
		pessoas.deleteAll();
		
		recrutador = new CargoT(null, "Recrutador");
		desenvolvedor = new CargoT(null, "Desenvolvedor");
		
		pf1 = new PessoaT(null, "Gustavo Oliveira", "1111111", TipoPessoaT.PESSOA_FISICA);
		pf2 = new PessoaT(null, "Joao Oliveira", "2342234", TipoPessoaT.PESSOA_FISICA);
		pj1 = new PessoaT(null, "Joao ME", "3423423", TipoPessoaT.PESSOA_JURIDICA);
		pj2 = new PessoaT(null, "Gustavo ME", "234234", TipoPessoaT.PESSOA_JURIDICA);
		
		f1 = new FuncionarioT(null, recrutador, pf1);
		f2 = new FuncionarioT(null, recrutador, pf2);
		f3 = new FuncionarioT(null, desenvolvedor, pj1);
		f4 = new FuncionarioT(null,  desenvolvedor,pj2);
		
		
	
		entity.persist(recrutador);
		entity.persist(desenvolvedor);
		entity.persist(pf1);
		entity.persist(pf2);
		entity.persist(pj1);
		entity.persist(pj2);
		entity.persist(f1);
		entity.persist(f3);
		entity.persist(f2);
		entity.persist(f4);

		
	}
	
	@org.junit.Test
	public void testQueryExample() {
		
			ExampleMatcher matcher = ExampleMatcher.matchingAny().withIgnoreCase();
			
			FuncionarioT fp = new FuncionarioT(null, recrutador, new PessoaT(null, "oliveira", null, null));
			
			List<FuncionarioT> result = funcionarios.findAll(Example.of(fp, matcher));
		
			assertThat(result, hasItems(f1,f2));
			assertThat(result, not(hasItems(f3,f4)));
	}
	

	
}
