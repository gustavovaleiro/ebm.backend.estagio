package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Estado;
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
		cidadeService.deleteAll();
		estado = new Estado(null, "GO", "Goias");
		cidade = new Cidade(null, "Goiania", estado);
		
	}
	
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
}
