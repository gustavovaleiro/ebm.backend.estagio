package com.ebm.pessoal.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cargo;

@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CargoServiceTest {
		
	@Autowired
	private CargoService cargoService;
	private Cargo c1;
	private Cargo c2;

	
	@Before
	public void setUp() {
		cargoService.deleteAll();
		c1 = new Cargo(null, "Scrum Master", BigDecimal.valueOf(4000), "Gerenciar a equipe de desenvolvimento");
		c2 = new Cargo(null, "Desenvolvedor", BigDecimal.valueOf(4000), "Desenvolver sistemas");
	}
	
	@Test
	public void testaInsercao() {
		c1 = cargoService.save(c1);
		
		assertNotNull(c1.getId());
	}
	@Test
	public void testUpdate() {
		c1 = cargoService.save(c1);
		c1.setNomeCargo("Auxiliar Administrativo");
		c1 = cargoService.save(c1);
		
		assertThat(c1.getNomeCargo(), equalTo("Auxiliar Administrativo"));
	} 
	@Test
	public void testaInsercaoEx() {
		c1.setNomeCargo(null);
		try {
			c1 = cargoService.save(c1);
			fail();
		}catch (DataIntegrityException e) {
			assertThat(e.getMessage(), equalTo(CargoService.DATAINTEGRITY_NOTNAME));
		}
	}
	
	@Test
	public void buscaPorId() {
		cargoService.save(c1);
		cargoService.save(c2);
		
		Cargo result = cargoService.findById(c1.getId());
	
		assertThat(c1, equalTo(result));
	}
	
	@Test
	public void buscaPorIdEx() {
		try {
			cargoService.findById(1);
			fail();
		}catch(ObjectNotFoundException e) {
			assertThat(e.getMessage(), equalTo(CargoService.ONFE_BYID + 1));
		}
	}
	
	@Test
	public void buscaNome() {
		c1.setNomeCargo("Desenvolvedor Java");
		cargoService.save(c1);
		cargoService.save(c2);
		Page<Cargo> results = cargoService.findByName("Desen", PageRequest.of(0, 2));
		
		assertThat(results.getContent().size(), equalTo(2));
		assertTrue(results.get().allMatch( c -> c.getNomeCargo().contains("Desenvolvedor")));
	}
	@Test
	public void buscaNome2() {
		cargoService.save(c1);
		cargoService.save(c2);
		Page<Cargo> results = cargoService.findByName("crUm", PageRequest.of(0, 2));
		results.forEach( c -> System.out.println(c.getNomeCargo()));
		assertThat(results.getContent().size(), equalTo(1));
		assertTrue(results.get().allMatch( c -> c.getNomeCargo() == "Scrum Master"));
	}
	@Test
	public void buscaNomeEx() {		
		try {
			cargoService.findByName(c2.getNomeCargo(), PageRequest.of(0, 2));
			fail();
		} catch (ObjectNotFoundException e) {
			assertThat(e.getMessage(), equalTo(CargoService.ONFE_BYNAME));
		}
		
	}

}
