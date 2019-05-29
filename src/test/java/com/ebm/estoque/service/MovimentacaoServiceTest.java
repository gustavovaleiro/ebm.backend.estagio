package com.ebm.estoque.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.Utils;
import com.ebm.estoque.domain.Item;
import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.estoque.domain.ProdutoMovimentacaoPK;
import com.ebm.estoque.domain.enums.TipoMovimentacao;
import com.ebm.estoque.dtos.MovimentacaoListDTO;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.MovimentacaoService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.geral.service.PopulaBD;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.Fornecedor;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.RG;
import com.ebm.pessoal.service.PessoaService;
import com.ebm.pessoal.service.interfaces.FornecedorService;


@ActiveProfiles("testauto")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MovimentacaoServiceTest {
	
	
	@Autowired
	private MovimentacaoService movimentacaoService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private CategoriaItemService catServ;
	@Autowired
	private UnidadeService uniServ;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private PopulaBD bd;
	
	private List<Item> produtos;
	
	private Fornecedor ff1;

	
	@Before
	public void setUp() {
	
		bd.instanciaMovimentacao(true);
		 produtos = Arrays.asList(bd.p1,bd.p2,bd.p3);
		 produtos.forEach( p -> ((Produto) p).setEstoque(5,0,10) );
		 
		catServ.save(bd.cat1);
		catServ.save(bd.cat2);
		catServ.save(bd.cat3);
		catServ.save(bd.cat4);
		uniServ.save(bd.un1);
		uniServ.save(bd.un2);
	}
	
	private Fornecedor fornecedor() {
		Estado estadoGO = new Estado(null, "GO", "Goias");
		Cidade goiania = new Cidade(null, "Goiania", estadoGO);
		Endereco endereco1 = new Endereco(null, "Test rua tal", "Centro", goiania, "123", "prox ao carai", "12345678",
				true,"Endereco residencial");
		PessoaFisica pf1 = new PessoaFisica(null, "Joao Da Silva", "02142627668", LocalDate.of(1990, 4, 30),
				new RG("23123", "SSP", estadoGO), "Brasileira", goiania);
		
		pf1.getEndereco().add(endereco1);
		pf1.getTelefone().add(Utils.getRandomTelefone(true));
		pf1.getEmail().add(Utils.getRandomEmail(pf1, true));
		pessoaService.save(pf1);
		Fornecedor ff1 = new Fornecedor(null, pf1);
		
		ff1.getCategorias().addAll(new HashSet<>(Arrays.asList(bd.cat1)));
		return ff1;
	}

	@Transactional
	@Test
	public void testeSalvandoMovimentacaoEntrada() {
		produtos = (List<Item>) itemService.saveAll(produtos);
		Fornecedor ff1 = fornecedor();
		 
			
		fornecedorService.save(ff1);
		bd.ent1.getFornecedores().add(ff1);
		bd.ent1 = movimentacaoService.save(bd.ent1);
		List<Item> result = itemService.findBy(null, null, null, null, null);
		
		assertNotNull(bd.ent1.getId());
		assertThat(bd.ent1.getTipoMovimentacao(), equalTo(TipoMovimentacao.ENTRADA));
		assertTrue(bd.ent1.getProdutoMovimentacao().stream().allMatch(pe -> pe.getMovimentacao().getId() == bd.ent1.getId()));
		assertTrue(bd.ent1.getProdutoMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getId() == p.getProduto().getId())));
		assertTrue(bd.ent1.getProdutoMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getNome() == p.getProduto().getNome())));
		assertTrue(bd.ent1.getProdutoMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getUnidade().getNome() == p.getProduto().getUnidade().getNome())));
		assertTrue(result.stream().allMatch( p ->  ((Produto) p).getEstoqueAtual().equals(5)));
		
	}

	
	
	// testar salvar saida com fornecedor e ver se toma exception
	@Transactional
	@Test
	public void testeSalvandoMovimentacaoSaidComFornecedorException() {
		produtos = (List<Item>) itemService.saveAll(produtos);
		Fornecedor ff1 = fornecedor();
		
		fornecedorService.save(ff1);
		bd.sai1.getFornecedores().add(ff1);
		try {
			bd.sai1 = movimentacaoService.save(bd.sai1);
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
		bd.sai1 = movimentacaoService.save(bd.sai1);
		List<Item> result = itemService.findBy(null, null, null, null, null);
		
		assertNotNull(bd.sai1.getId());
		assertThat(bd.sai1.getTipoMovimentacao(), equalTo(TipoMovimentacao.SAIDA));
		assertTrue(bd.sai1.getProdutoMovimentacao().stream().allMatch(pe -> pe.getMovimentacao().getId() == bd.sai1.getId()));
		assertTrue(bd.sai1.getProdutoMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getId() == p.getProduto().getId())));
		assertTrue(bd.sai1.getProdutoMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getNome() == p.getProduto().getNome())));
		assertTrue(bd.sai1.getProdutoMovimentacao().stream().allMatch( p -> produtos.stream().anyMatch( pS -> pS.getUnidade().getNome() == p.getProduto().getUnidade().getNome())));
		assertTrue(result.stream().allMatch( p ->  ((Produto) p).getEstoqueAtual().equals(-5)));
		
	}
	
	@Transactional
	@Test
	public void testeSalvarSemProduto() {
		produtos = (List<Item>) itemService.saveAll(produtos);

		bd.sai1.getProdutoMovimentacao().clear();
		
		try {
			bd.sai1 = movimentacaoService.save(bd.sai1);
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
		en.getProdutoMovimentacao().add( new ProdutoMovimentacao(new ProdutoMovimentacaoPK((Produto) produtos.get(0), en), BigDecimal.valueOf(0), BigDecimal.valueOf(10), 4));
		movimentacaoService.save(en);
		int estoqPosEntrada = ((Produto) itemService.findById(produtos.get(0).getId())).getEstoqueAtual();
	
		Movimentacao sa = Movimentacao.novaSaida();
		sa.getProdutoMovimentacao().add(  new ProdutoMovimentacao(new ProdutoMovimentacaoPK((Produto) produtos.get(0), en), BigDecimal.valueOf(0), BigDecimal.valueOf(10),2));
		movimentacaoService.save(sa);
		
		
	    Produto p = (Produto) itemService.findById(produtos.get(0).getId());
		assertThat(p.getEstoqueAtual(), equalTo(2));
		assertThat(estoqPosEntrada, equalTo(4));
	}
	
	@Transactional
	@Test
	public void testFindById() {
		produtos = (List<Item>) itemService.saveAll(produtos);
		
		bd.ent1 = movimentacaoService.save(bd.ent1);
		
		Movimentacao result = movimentacaoService.findById(bd.ent1.getId());
		
		assertThat(bd.ent1.getId(), equalTo(result.getId()));
	}
	@Transactional
	@Test
	public void testFindByIdExNull() {

		try {
			 movimentacaoService.findById(null);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(MovimentacaoService.DATAINTEGRITY_IDNULL));
		}
	}
	
	@Transactional
	@Test
	public void testFindByIdExNotFound() {

		try {
		movimentacaoService.findById(1);
			fail();
		}catch(ObjectNotFoundException ex) {
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

	
		produtos = (List<Item>) itemService.saveAll(produtos);
		itemService.saveAll(Arrays.asList(bd.p4,bd.p5,bd.p6,bd.p7));
		
		
		bd.ent2.setDocumento("notafiscal-01");
		bd.ent3.setDocumento("notafiscal-01");
		bd.sai2.setDocumento("Venda01");
		bd.sai3.setDocumento("Venda02");
		
		ff1 = fornecedorService.save(fornecedor());
		
		bd.ent2.getFornecedores().add(ff1);

		movimentacaoService.saveAll(Arrays.asList(bd.ent1,bd.ent2,bd.ent3,bd.sai1,bd.sai2,bd.sai3));
	}
	
	//test find por tipo E
	@Transactional
	@Test
	public void testFindParameterizadoEntrada() {
		preparaCenarioBuscaParamiterizada();

		Page<MovimentacaoListDTO> result = movimentacaoService.findBy(TipoMovimentacao.ENTRADA, null, null,null, PageRequest.of(0, 8));
		
		assertThat(result.getNumberOfElements(), equalTo(3));
		assertTrue(result.stream().allMatch(m -> m.getTipo()== TipoMovimentacao.ENTRADA.getDesc()));
		assertFalse(result.stream().anyMatch(m -> m.getTipo()== TipoMovimentacao.SAIDA.getDesc()));
	}
	
	//test find por tipo S
	@Transactional
	@Test
	public void testFindParameterizadoSaida() {
		preparaCenarioBuscaParamiterizada();

		Page<MovimentacaoListDTO> result = movimentacaoService.findBy(TipoMovimentacao.SAIDA, null, null,null, PageRequest.of(0, 8));
		
		assertThat(result.getNumberOfElements(), equalTo(3));
		assertTrue(result.stream().allMatch(m -> m.getTipo()== TipoMovimentacao.SAIDA.getDesc()));
		assertFalse(result.stream().anyMatch(m -> m.getTipo()== TipoMovimentacao.ENTRADA.getDesc()));
	}
	//tes find por document return e2 e e3
	@Transactional
	@Test
	public void testFindParameterizadoDocumento() {
		preparaCenarioBuscaParamiterizada();
		
		Page<MovimentacaoListDTO> result = movimentacaoService.findBy(null, "notafiscal", null,null, PageRequest.of(0, 8));
		
		assertThat(result.getNumberOfElements(), equalTo(2));
		assertTrue(result.stream().allMatch(m -> m.getTipo()== TipoMovimentacao.ENTRADA.getDesc()));
		assertFalse(result.stream().anyMatch(m -> m.getTipo()== TipoMovimentacao.SAIDA.getDesc()));
		assertTrue(result.stream().allMatch(m -> m.getDocumento().toLowerCase().contains("notafiscal")));
	}
	//test find por fornecedor e2 s2
	@Transactional
	@Test
	public void testFindParameterizadoFornecedor() {
		preparaCenarioBuscaParamiterizada();
		
		Page<MovimentacaoListDTO> result = movimentacaoService.findBy(null, null, Arrays.asList(ff1.getId()),null, PageRequest.of(0, 8));
		
		assertThat(result.getNumberOfElements(), equalTo(1));
		assertTrue(result.stream().anyMatch(m -> m.getId().equals(bd.ent2.getId())));
	}
	
	//test find por produto e2 s2 S3 E3
	@Transactional
	@Test
	public void testFindParameterizadoProduto() {
		preparaCenarioBuscaParamiterizada();
		
		Page<MovimentacaoListDTO> result = movimentacaoService.findBy(null, null, null,Arrays.asList(bd.p4.getId(), bd.p5.getId()), PageRequest.of(0, 8));
		
		assertThat(result.getNumberOfElements(), equalTo(4));
		
		assertTrue(result.stream().anyMatch(m -> Arrays.asList(bd.ent2,bd.sai2,bd.ent3,bd.sai3).stream().anyMatch(i -> m.getId().equals(i.getId()))));

	}
	
	//test find tipo e produto E3
	@Transactional
	@Test
	public void testFindParameterizadoProdutoETipo() {
		preparaCenarioBuscaParamiterizada();
		
		Page<MovimentacaoListDTO> result = movimentacaoService.findBy(TipoMovimentacao.ENTRADA, null, null,Arrays.asList(bd.p4.getId(), bd.p5.getId()), PageRequest.of(0, 8));
		
		assertThat(result.getNumberOfElements(), equalTo(2));
		assertTrue(result.stream().anyMatch(m -> Arrays.asList(bd.ent2,bd.ent3).stream().anyMatch(i -> m.getId().equals(i.getId()))));
	}
	//test find tipo e fornecedor
	@Transactional
	@Test
	public void testFindParameterizadoFornecedorEtipo() {
		preparaCenarioBuscaParamiterizada();
		
		Page<MovimentacaoListDTO> result = movimentacaoService.findBy(TipoMovimentacao.SAIDA, null, Arrays.asList(ff1.getId()),null, PageRequest.of(0, 8));
		
		assertThat(result.getNumberOfElements(), equalTo(0));
	}
	
	//testFindAllNull
	@Transactional
	@Test
	public void testFindParamiterizadoAllNull() {
	preparaCenarioBuscaParamiterizada();
		
		Page<MovimentacaoListDTO> result = movimentacaoService.findBy(null, null, new ArrayList<>(), new ArrayList<>(), PageRequest.of(0, 8));
		
		assertThat(result.getNumberOfElements(), equalTo(6));
		assertTrue(result.stream().anyMatch(m -> Arrays.asList(bd.ent1,bd.ent2,bd.ent3,bd.sai1,bd.sai2,bd.sai3).stream().anyMatch(i -> m.getId().equals(i.getId()))));
	}
	
	@Transactional
	@Test
	public void testDeleteNotFound() {
		try {
			movimentacaoService.deleteById(2);
			fail();
		}catch(ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(MovimentacaoService.ONFE_NOTFOUNDBYID+2));
		}
	}
	
	@Transactional
	@Test
	public void testDeleteExIdNull() {
		try {
			movimentacaoService.deleteById(null);
			fail();
		}catch(DataIntegrityException ex) {
			assertThat(ex.getMessage(), equalTo(MovimentacaoService.DATAINTEGRITY_IDNULL));
		}
	}
	
	@Transactional
	@Test
	public void testDelete() {
		itemService.saveAll(produtos);
		bd.ent1 = movimentacaoService.save(bd.ent1);
		int id = bd.ent1.getId();
		movimentacaoService.deleteById(id);
		try {
			movimentacaoService.findById(id);
			fail();
		}catch(ObjectNotFoundException ex) {
			assertThat(ex.getMessage(), equalTo(MovimentacaoService.ONFE_NOTFOUNDBYID+id));
		}
	}
	
}
