//package com.ebm.services;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//import org.springframework.stereotype.Service;
//
//import com.ebm.auth.Usuario;
//import com.ebm.comercial.domain.ProdutoVenda;
//import com.ebm.comercial.domain.Venda;
//import com.ebm.estoque.domain.CategoriaItem;
//import com.ebm.estoque.domain.Produto;
//import com.ebm.estoque.domain.Unidade;
//import com.ebm.pessoal.domain.Cargo;
//import com.ebm.pessoal.domain.Cidade;
//import com.ebm.pessoal.domain.Cliente;
//import com.ebm.pessoal.domain.Endereco;
//import com.ebm.pessoal.domain.Estado;
//import com.ebm.pessoal.domain.Funcionario;
//import com.ebm.pessoal.domain.PessoaFisica;
//import com.ebm.pessoal.domain.RG;
//import com.ebm.pessoal.domain.TipoEndereco;
//
//@Service
//public class DBService {
//
//	//@Autowired
//	//private VendaRepository vendas;
//	
//	
//	private Estado est1;
//	private Cidade cid1;
//	private Endereco end1;
//	private PessoaFisica pf;
//	private Cliente cli1;
//	private Venda v1;
//	private Usuario user1;
//	private CategoriaItem cat1;
//	private CategoriaItem cat2;
//	private Unidade uni1;
//	private Produto prod1;
//	private Produto prod2;
//	private ProdutoVenda prodVen1;
//	private ProdutoVenda prodVen2;
//
//
//	private PessoaFisica pf2;
//
//
//	private Cargo carg1;
//
//
//	private Funcionario func1;
//	
//	public void InitiateObjects() {
//		
//		//	PACOTE PESSOA
//		est1 = new Estado(null, "GO", "Goiás");
//		cid1 = new Cidade(null, "Santa Rita", est1);
//		end1 = new Endereco(null, "Epifaneo", "Centro", cid1, "321", "test", "7584000", new TipoEndereco("Residencial"));
//		pf = new PessoaFisica(null, "Joao TESTE", "05905961162", LocalDate.of(1990, 7, 20), new RG("231231", "SSP", est1), "brasileira", cid1);
//		pf2 = new PessoaFisica(null, "Maria", "05905421162", LocalDate.of(1990, 7, 20), new RG("23fd31", "SSP", est1), "brasileira", cid1);
//		cli1 = new Cliente(null,pf , BigDecimal.valueOf(1999), "Cliente Test");
//		carg1 = new Cargo(null, "Vendedor", BigDecimal.valueOf(1000));
//		func1 = new Funcionario(null, pf2, "1231",carg1 , LocalDate.now(), 0.2, BigDecimal.valueOf(100));
//		user1 = new Usuario(null, "logintest", "test@gmail.com", "aasdf", null);
//		user1.setFuncionario(func1);
//		
//		
//		//PACOTE COMERCIAL
//		v1 = new Venda(null, LocalDate.now(), null, cli1, null, "Venda de test 1 ");
//		prodVen1 = new ProdutoVenda(prod1, v1, 2, BigDecimal.valueOf(10), BigDecimal.valueOf(0));
//		prodVen2 = new ProdutoVenda(prod2, v1, 1, BigDecimal.valueOf(300), BigDecimal.valueOf(0));
//		v1.getProdutosVenda().addAll(Arrays.asList(prodVen1, prodVen2));
//		
//		// PACOTE PRODUTO
//		cat1 = new CategoriaItem(null, "Informatica");
//		cat2 = new CategoriaItem(null, "Smartphone e Celulares");
//		uni1 = new Unidade(null, "UN", "unidade");	
//		prod1 = new Produto(null, "Mouse", "Mouse USB 3 BOTÕES", uni1,cat1 , "213sd", 0.40, 10, LocalDateTime.now(), user1);
//		prod1.setValorCompraMedio(BigDecimal.valueOf(5));
//		prod2 = new Produto(null, "XIAOMI MI A2", "XIAOMI MI A2 PROC QUADCORE 3GB RAM 32GB ARM", uni1,cat2 , "2134sd", 0.30, 5, LocalDateTime.now(), user1);
//		prod1.setValorCompraMedio(BigDecimal.valueOf(200));
//		
//		
//		
//		
//		
//	}
//}
