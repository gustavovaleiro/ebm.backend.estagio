package com.ebm.security;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebm.geral.service.PopulaBD;

@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UsuarioEGrupoTest {

	@Autowired
	private PopulaBD bd;

	@Before
	public void setUp() {

		bd.instanciaFuncionario(true).instanciaGrupo().instanciaUsuarios();
		bd.user1.setId(1);
		bd.user2.setId(2);
		bd.grup1.setId(1);
		bd.grup2.setId(2);
		bd.user1.setGrupo(bd.grup1);
		bd.user2.setGrupo(bd.grup2);
	}
	
	@Test
	public void testContains() {
		assertTrue(bd.grup1.getUsuarios().contains(bd.user1));
		assertTrue(bd.grup2.getUsuarios().contains(bd.user2));
		assertTrue(bd.user1.getGrupo().equals(bd.grup1));
		assertTrue(bd.user2.getGrupo().equals(bd.grup2));
		assertFalse(bd.grup1.getUsuarios().contains(bd.user2));
		assertFalse(bd.grup2.getUsuarios().contains(bd.user1));
	}
	
	@Test
	public void testTrocaGrupoPeloUsuario() {
		bd.user1.setGrupo(bd.grup2);
		bd.user2.setGrupo(bd.grup1);
		
		assertTrue(bd.grup2.getUsuarios().contains(bd.user1));
		assertTrue(bd.grup1.getUsuarios().contains(bd.user2));
		assertTrue(bd.user1.getGrupo().equals(bd.grup2));
		assertTrue(bd.user2.getGrupo().equals(bd.grup1));
		assertFalse(bd.grup1.getUsuarios().contains(bd.user1));
		assertFalse(bd.grup2.getUsuarios().contains(bd.user2));
	}
	
	
	
}
