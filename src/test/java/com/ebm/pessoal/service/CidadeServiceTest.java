package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.service.CidadeService;
import com.ebm.pessoal.service.EstadoService;
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CidadeServiceTest {

	@Autowired
	private CidadeService cidadeService;
	@Autowired
	private EstadoService estadoService;
	private Cidade cidade;
	private Estado estado;
	
	@Before
	public void setUp() {
		cidadeService.deleteAll(true);
		estado = new Estado(null, "GO", "Goias");
		cidade = new Cidade(null, "Goiania", estado);
		
	}
	//testes de insert
	@Test
	public void salvaCidadeSemEstado() {
		cidade.setEstado(null);
		
		try {
			cidadeService.save(cidade);
			fail();
		} catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo( CidadeService.CIDADE_ESTADO_INVALIDO) );
		}
	
	}
	@Test
	public void salvaCidadeNovaComEstadoNovo() {
		cidadeService.save(cidade);
		
		assertNotNull(cidade.getId());
		assertNotNull(estado.getId());
	}
	@Test
	public void salvaCidadeNovaComEstadoEditado() {
		
		estadoService.save(estado);
		estado.setUF("MT").setNome("Mato Grosso");
	
	    cidadeService.save(cidade);
		
		assertNotNull(cidade.getId());
		assertNotNull(estado.getId());
		assertThat(cidade.getEstado().getUF(), equalTo("MT"));
	}
	
	@Test
	public void updateCidadeTrocarEstadoNome() {
		cidadeService.save(cidade);
		
		assertNotNull(cidade.getId());
		
		cidade.setEstado(new Estado(null, "PA", "PARA"));
		cidade.setNome("Belem");
		
		cidadeService.save(cidade);
		
		assertNotNull(cidade.getEstado().getId());
		assertThat(cidade.getEstado().getUF(), equalTo(cidade.getEstado().getUF()));
		assertThat(cidade.getNome(), equalTo("Belem"));
		
	}
	
	@Test
	public void findById() {
		cidadeService.save(cidade);
		
		Cidade retorno  = cidadeService.findById(cidade.getId());
		
		assertThat(cidade.getNome(), equalTo(retorno.getNome()));
		
	}
	@Test
	public void findByIdLancaException() {
		try {
			cidadeService.findById(1);
			fail();
		}catch(ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(cidadeService.CIDADE_NOTFOUND_ID + 1));
		}
		
	}
	
	@Test
	public void findByEstado() {
		Cidade cidade2 = new Cidade(null, "Santa Rita", estado);
		Cidade cidade3 = new Cidade(null, "Mineiros", estado);
		Cidade cidade4 = new Cidade(null, "Araguaia", new Estado(null, "MT", "Mato Grosso"));
		
		cidadeService.saveAll(Arrays.asList(cidade, cidade2,cidade3,cidade4));
		
		List<Cidade> cidadeGO = cidadeService.findByEstado(estado.getUF());
		List<Cidade> cidadeMT = cidadeService.findByEstado("MT");
		
		assertThat(cidadeGO.size(), equalTo(3));
		assertThat(cidadeMT.size(), equalTo(1));
	}
	@Test
	public void findByEstadoSemCidade() {
		estadoService.save(new Estado(null, "PR", "PARANA"));
		
		try {
			cidadeService.findByEstado("PR");
			fail();
		} catch (ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(CidadeService.CIDADE_NOTFOUND_UF + "PR"));
		}
	}
}
