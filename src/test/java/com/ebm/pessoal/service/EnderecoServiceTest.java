package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebm.BaseTest;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;

public class EnderecoServiceTest  extends BaseTest{
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private CidadeService cidadeService;

	private Cidade cidade;

	private Endereco endereco;
	
	@Before
	public void setUp() {
		enderecoService.deleteAll();
		cidadeService.deleteAll(true);
		cidade = new Cidade(null, "Cuiaba", new Estado(null, "MT", "Mato Grosso"));
		endereco = new Endereco(null, "Rua tal", "Bairro", cidade, "123", "test", "32432432", true, "Comercial");
		
	}
	
	@After
	public void setDown() {
		enderecoService.deleteAll();
		cidadeService.deleteAll(true);
	}
	@Test
	public void SalvaEnderecoComCidade() {
		Endereco result = enderecoService.save(endereco);
		
		assertNotNull(result.getId());
		assertNotNull(result.getCidade().getId());	
	}
	@Test
	public void salvaEnderecoSemCidade() {
		
		try {
			endereco.setCidade(null);
			enderecoService.save(endereco);
			fail();
		}catch (DataIntegrityException e) {
			assertThat(e.getMessage(), equalTo(EnderecoService.DATAINTEGRITY_ENDERECOCIDADE));
		}
	}
	
	@Test
	public void salvaEnderecoComCidadeSalva() {
		endereco.setCidade(null);
		cidade  = cidadeService.save(cidade);
		
		endereco.setCidade(cidade);
		enderecoService.save(endereco);
	
		assertNotNull(endereco.getId());
		assertNotNull(endereco.getCidade().getId());	
	}
	
	@Test
	public void testaUpdateComCidadeNova() {
		enderecoService.save(endereco);
		endereco.setBairro("Centro");
		endereco.setCidade(new Cidade(null, "Mineiros", new Estado(null, "GO", "Goias")));
		Endereco result = enderecoService.save(endereco);
		
		assertThat(result.getBairro(), equalTo("Centro"));
		assertThat(result.getCidade().getNome(), equalTo("Mineiros"));
		assertNotNull(result.getCidade().getId());
	}
	
	@Test
	public void testaFindTiposEndereco() {
		String tipo1 = "Comercial";
		String tipo2 = "Industrial";
		String tipo3 = "Residencial";
		
		enderecoService.save(endereco);
		enderecoService.save(new Endereco(null, "Rua tal", "Bairro", cidade, "123", "test", "32432432", true,tipo2));
		enderecoService.save(new Endereco(null, "Rua tal", "Bairro", cidade, "123", "test", "32432432", true,tipo3));
	
		List<String> tipos = enderecoService.getTipoEndereco();
		
		assertThat(tipos.size(), equalTo(3));
		assertTrue(tipos.contains(tipo1));
		assertTrue(tipos.contains(tipo2));
		assertTrue(tipos.contains(tipo3));
	}
}
