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
import java.util.stream.Collectors;import org.aspectj.weaver.NewParentTypeMunger;
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
import com.ebm.exceptions.ObjectNotFoundException;
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
	private Unidade un1;
	private Produto p4;
	private Produto p5;
	private Produto p6;
	private Produto p7;
	private Movimentacao e2;
	private Movimentacao e3;
	private Movimentacao s2;
	private Movimentacao s3;

	
	@Before
	public void setUp() {
	
		 un1 = new Unidade(null, "un", "Unidade");
		cat1 = new CategoriaItem(null, "Informatica");

		 p1 = Produto.of("Computador I3 4gb RAM", un1, cat1);
		 p2 = Produto.of("Processador I3", un1, cat1);
		 p3 = Produto.of("Memoria ram 4GB", un1, cat1);
		 
		 
		
		 produtos = Arrays.asList(p1,p2,p3);
		 produtos.forEach( p -> ((Produto) p).setEstoque(5,0,10) );
		 e1 = Movimentacao.novaEntrada();
		 s1  = Movimentacao.novaSaida();
		 
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

	@Transactional
	@Test
	public void testQuantidadeEmEstoque()
	{
		produtos = (List<Item>) itemService.saveAll(produtos);
		
		Movimentacao en = Movimentacao.novaEntrada(); 
		en.getProdutosMovimentacao().add( new ProdutoMovimentacao(new ProdutoMovimentacaoPK((Produto) produtos.get(0), en), BigDecimal.valueOf(0), BigDecimal.valueOf(10), 4));
		movimentacaoService.save(en);
		int estoqPosEntrada = ((Produto) itemService.findById(produtos.get(0).getId())).getEstoqueAtual();
	
		Movimentacao sa = Movimentacao.novaSaida();
		sa.getProdutosMovimentacao().add(  new ProdutoMovimentacao(new ProdutoMovimentacaoPK((Produto) produtos.get(0), en), BigDecimal.valueOf(0), BigDecimal.valueOf(10),2));
		movimentacaoService.save(sa);
		
		
	    Produto p = (Produto) itemService.findById(produtos.get(0).getId());
		assertThat(p.getEstoqueAtual(), equalTo(2));
		assertThat(estoqPosEntrada, equalTo(4));
	}
	
	@Transactional
	@Test
	public void testFindById() {
		produtos = (List<Item>) itemService.saveAll(produtos);
		
		e1 = movimentacaoService.save(e1);
		
		Movimentacao result = movimentacaoService.findById(e1.getId());
		
		assertThat(e1.getId(), equalTo(result.getId()));
	}
	@Transactional
	@Test
	public void testFindByIdExNull() {

		try {
			Movimentacao result = movimentacaoService.findById(null);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(MovimentacaoService.DATAINTEGRITY_IDNULL));
		}
	}
	
	@Transactional
	@Test
	public void testFindByIdExNotFound() {

		try {
			Movimentacao result = movimentacaoService.findById(1);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(MovimentacaoService.ONFE_NOTFOUNDBYID + 1));
		}
	}
	
	//E1 -> P1 P2 P3;
	//S1 -> P1 P2 P3;
	//e2 -> P4 P5
	//E3 -> P4 P6 P7
	//S2 -> P4 P5;
	//S3 -> P4 P6P7
	private void preparaCenarioBuscaParamiterizada() {
		p4 = Produto.of("Computador i5 8GB", un1, cat1);
		p5 = Produto.of("Mouse", un1, cat1);
		p6 = Produto.of("Teclado", un1, cat1);
		p7 = Produto.of("Processador i5", un1, cat1);
		
		produtos.addAll(Arrays.asList(p4,p5,p5,p7));
		produtos = (List<Item>) itemService.saveAll(produtos);
		
		
		e2 = Movimentacao.novaEntrada();
		e3 = Movimentacao.novaEntrada();
		s2 = Movimentacao.novaSaida();
		s3 = Movimentacao.novaSaida();
		
		e2.setDocumento("notafiscal-01");
		e3.setDocumento("notafiscal-01");
		s2.setDocumento("Venda01");
		s3.setDocumento("Venda02");
		
		Arrays.asList(e2,e3,s2,s3).forEach(m ->m.getProdutosMovimentacao()
			.add(new ProdutoMovimentacao(new ProdutoMovimentacaoPK(p4, m), BigDecimal.valueOf(0), BigDecimal.valueOf(10), 10))
		); 
		
		Arrays.asList(e2,s2).forEach(m ->m.getProdutosMovimentacao()
				.add(new ProdutoMovimentacao(new ProdutoMovimentacaoPK(p5, m), BigDecimal.valueOf(0), BigDecimal.valueOf(10), 5))
		); 
		
		Arrays.asList(e3,s3).forEach(m -> m.getProdutosMovimentacao()
				.addAll( Arrays.asList(p6,p7).stream().map( p -> new ProdutoMovimentacao
						(new ProdutoMovimentacaoPK(((Produto)p), m), BigDecimal.valueOf(0), BigDecimal.valueOf(10), 4)).collect(Collectors.toSet())));
	
		movimentacaoService.saveAll(Arrays.asList(e1,e2,e3,s1,s2,s3));
	}
	
	//test find por tipo E
	//test find por tipo S
	//tes find por document
	//test find por fornecedor
	//test find por produto
	//test find tipo e produto
	//test find tipo e fornecedor
	
	
}
