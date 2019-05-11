package com.ebm.estoque.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.Utils;
import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Fornecedor;
import com.ebm.estoque.domain.Item;
import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.estoque.domain.ProdutoMovimentacaoPK;
import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.estoque.service.interfaces.FornecedorService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.MovimentacaoService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.RG;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MovimentacaoServiceTest {
	
	
	@Autowired
	private MovimentacaoService movimentacaoService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private FornecedorService fornecedorService;
	private Produto p1;
	private Produto p2;
	private Produto p3;
	private Movimentacao e1;
	private Movimentacao s1;
	private List<Item> produtos;
	private CategoriaItem cat1;

	
	@Before
	public void setUp() {
	
		Unidade un1 = new Unidade(null, "un", "Unidade");
		cat1 = new CategoriaItem(null, "Informatica");

		 p1 = Produto.of("Computador", un1, cat1);
		 p2 = Produto.of("Processador", un1, cat1);
		 p3 = Produto.of("Memoria", un1, cat1);
		 
		 
		
		 produtos = Arrays.asList(p1,p2,p3);
		 produtos.forEach( p -> ((Produto) p).setEstoque(5,0,10) );
		 e1 = Movimentacao.deEntrada();
		 s1  = Movimentacao.deSaida();
		 
		 Arrays.asList(e1,s1).forEach( m -> m.getProdutosMovimentacao().addAll( produtos.stream()
				 .map( p -> new ProdutoMovimentacao( new ProdutoMovimentacaoPK((Produto) p, e1), BigDecimal.valueOf(10), BigDecimal.valueOf(100), 5)).collect(Collectors.toSet())  ));

		
	}
	
	private Fornecedor fornecedor() {
		Estado estadoGO = new Estado(null, "GO", "Goias");
		Cidade goiania = new Cidade(null, "Goiania", estadoGO);
		Endereco endereco1 = new Endereco(null, "Test rua tal", "Centro", goiania, "123", "prox ao carai", "12345678",
				"Endereco residencial", true);
		PessoaFisica pf1 = new PessoaFisica(null, "Joao Da Silva", "02142627668", LocalDate.of(1990, 4, 30),
				new RG("23123", "SSP", estadoGO), "Brasileira", goiania);
		
		pf1.getEndereco().add(endereco1);
		pf1.getTelefone().add(Utils.getRandomTelefone(true));
		pf1.getEmail().add(Utils.getRandomEmail(pf1, true));
		Fornecedor ff1 = new Fornecedor(null, pf1);
		ff1.getCategorias().addAll(new HashSet<>(Arrays.asList(cat1)));
		return ff1;
	}

	@Transactional
	@Test
	public void testeSalvandoMovimentacaoEntrada() {
		produtos = (List<Item>) itemService.saveAll(produtos);
		Fornecedor ff1 = fornecedor();
		 
			
		fornecedorService.save(ff1);
		e1.getFornecedores().add(ff1);
		e1 = movimentacaoService.save(e1);
		List<Item> result = itemService.findBy(null, null, null, null, null);
		
		assertNotNull(e1.getId());
		assertThat(e1.getTipoMovimentacao(), equalTo(TipoMovimentacao.ENTRADA));
		assertTrue(e1.getProdutosMovimentacao().stream().allMatch(pe -> pe.getMovimentacao().getId() == e1.getId()));
		assertTrue(e1.getProdutosMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getId() == p.getProduto().getId())));
		assertTrue(e1.getProdutosMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getNome() == p.getProduto().getNome())));
		assertTrue(e1.getProdutosMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getUnidade().getNome() == p.getProduto().getUnidade().getNome())));
		assertTrue(result.stream().allMatch( p ->  ((Produto) p).getEstoqueAtual().equals(5)));
		
	}

	
	
	// testar salvar saida com fornecedor e ver se toma exception
	@Transactional
	@Test
	public void testeSalvandoMovimentacaoSaidComFornecedorException() {
		produtos = (List<Item>) itemService.saveAll(produtos);
		Fornecedor ff1 = fornecedor();
		
		fornecedorService.save(ff1);
		s1.getFornecedores().add(ff1);
		try {
			s1 = movimentacaoService.save(s1);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(MovimentacaoService.DATAINTEGRITY_SAIDAWITHFORNECEDOR));
		}
		
	}

	
	// salvar entrada com fornecedor e ver q n toma exception
	@Transactional
	@Test
	public void testeSalvandoMovimentacaoSaida() {
		produtos = (List<Item>) itemService.saveAll(produtos);
		s1 = movimentacaoService.save(s1);
		List<Item> result = itemService.findBy(null, null, null, null, null);
	
		assertNotNull(s1.getId());
		assertThat(s1.getTipoMovimentacao(), equalTo(TipoMovimentacao.SAIDA));
		assertTrue(s1.getProdutosMovimentacao().stream().allMatch(pe -> pe.getMovimentacao().getId() == s1.getId()));
		assertTrue(s1.getProdutosMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getId() == p.getProduto().getId())));
		assertTrue(s1.getProdutosMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getNome() == p.getProduto().getNome())));
		assertTrue(s1.getProdutosMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getUnidade().getNome() == p.getProduto().getUnidade().getNome())));
		assertTrue(result.stream().allMatch( p ->  ((Produto) p).getEstoqueAtual().equals(-5)));
		
	}
	
	@Transactional
	@Test
	public void testeSalvarSemProduto() {
		produtos = (List<Item>) itemService.saveAll(produtos);

		s1.getProdutosMovimentacao().clear();
		
		try {
			s1 = movimentacaoService.save(s1);
			fail();
		}catch (DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(MovimentacaoService.DATAINTEGRITY_NOPRODUTOS));
		}
	
	}


//	@Test
//	public void testaAdicionarCategoriaSemNomeEx() {
//		cat1.setNome(null);
//		try {
//			cat1 = movimentacaoService.save(cat1);
//			fail();
//		}catch(DataIntegrityException ex) {
//			assertThat(ex.getMessage(), equalTo(CategoriaItemService.DATAINTEGRITY_NOMENULL));
//		}
//		
//	}
//	@Test
//	public void testaAdicionarCategoriaNomeRepetido() {
//		CategoriaItem un2 = new CategoriaItem(null, cat1.getNome());
//		movimentacaoService.save(cat1);
//		try {
//			un2 = movimentacaoService.save(un2);
//			fail();
//		}catch(DataIntegrityException ex) {
//			assertThat(ex.getMessage(), equalTo(CategoriaItemService.DATAINTEGRITY_DUPLICATENOME));
//		}
//		
//	}
//	
//	@Test
//	public void testeUpdate() {
//		cat1 = movimentacaoService.save(cat1);
//		cat1.setNome("outronome");
//		CategoriaItem un2 = movimentacaoService.save(cat1);
//		
//		assertNotNull(cat1.getId());
//		assertThat(un2.getId(), equalTo(cat1.getId()));
//		assertThat(un2.getNome(), equalTo("outronome"));
//	}
	
	
	
	
}
