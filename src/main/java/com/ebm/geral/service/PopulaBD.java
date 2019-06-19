package com.ebm.geral.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Movimentacao;
import com.ebm.estoque.domain.Produto;
import com.ebm.estoque.domain.ProdutoMovimentacao;
import com.ebm.estoque.domain.ProdutoMovimentacaoPK;
import com.ebm.estoque.domain.Servico;
import com.ebm.estoque.domain.Unidade;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.MovimentacaoService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.Fornecedor;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.RG;
import com.ebm.pessoal.service.CargoService;
import com.ebm.pessoal.service.ClienteService;
import com.ebm.pessoal.service.FuncionarioService;
import com.ebm.pessoal.service.PessoaService;
import com.ebm.pessoal.service.interfaces.FornecedorService;
import com.ebm.security.Modulo;
import com.ebm.security.PermissaoE;
import com.ebm.security.Usuario;
import com.ebm.security.service.UsuarioService;

@Service
public class PopulaBD {

	public Estado estadoGO;
	public Cidade goiania;
	public Endereco endereco1;
	public PessoaFisica pf1;
	public PessoaJuridica pj1;
	public Cargo cDesenvolvedor;
	public Cargo cAdministrador;
	public Funcionario funf1;
	public Funcionario funj1;
	public PessoaFisica pf2;
	public PessoaFisica pf3;
	public PessoaFisica pf4;
	public PessoaJuridica pj2;
	public PessoaJuridica pj3;
	public PessoaJuridica pj4;
	public Funcionario funf2;
	public Funcionario funf3;
	public Funcionario funf4;
	public Funcionario funj2;
	public Funcionario funj3;
	public Funcionario funj4;
	public Cliente cf1;
	public Cliente cj1;
	public Cliente cf2;
	public Cliente cf3;
	public Cliente cf4;
	public Cliente cj2;
	public Cliente cj3;
	public Cliente cj4;
	public CategoriaItem cat1;
	public CategoriaItem cat3;
	public CategoriaItem cat2;
	public CategoriaItem cat4;
	public Fornecedor forf1;
	public Fornecedor forj1;
	public Fornecedor forf2;
	public Fornecedor forf3;
	public Fornecedor forf4;
	public Fornecedor forj2;
	public Fornecedor forj3;
	public Fornecedor forj4;
	public Fornecedor forf5;
	public PessoaFisica pf5;
	public Unidade un1;
	public Unidade un2;
	public Produto p1;
	public Servico s1;
	public Produto p2;
	public Produto p3;
	public Produto p4;
	public Servico s2;
	public Servico s3;
	public Servico s4;
	public Movimentacao ent1;
	public Movimentacao sai1;
	public Produto p5;
	public Produto p6;
	public Produto p7;
	public Movimentacao ent3;
	public Movimentacao ent2;
	public Movimentacao sai2;
	public Movimentacao sai3;
	public Usuario user1;
	public Usuario user2;
	public Usuario user3;
	public Usuario user4;
	@Autowired
	private PessoaService pessoaS;
	@Autowired
	private ClienteService clientS;
	@Autowired
	private FornecedorService fornecedorS;
	@Autowired
	private FuncionarioService funcionarioS;
	@Autowired
	private CargoService cargoS;

	@Autowired
	private UnidadeService unidadeS;
	@Autowired
	private CategoriaItemService categoriaS;
	@Autowired
	private ItemService itemS;
	@Autowired
	private MovimentacaoService movimentacaoS;
	@Autowired
	private UsuarioService usuarioService;
	public Usuario user5;




	public PopulaBD populaBD() {
		instanciaPessoa();
		associaPessoa();
		instanciaCliente(false);
		instanciaCategorias();
		instanciaFornecedores(false);
		instanciaFuncionario(false);

		pessoaS.saveAll(Arrays.asList(pf1, pf2, pf3, pf4, pj1, pj2, pj3, pj4));
		clientS.saveAll(Arrays.asList(cf1, cf2, cf3, cf4, cj1, cj2, cj3, cj4));
		categoriaS.saveAll(Arrays.asList(cat1, cat2, cat3, cat4));
		fornecedorS.saveAll(Arrays.asList(forf1, forf2, forf3, forf4, forj1, forj2, forj3, forj4));
		cargoS.save(cAdministrador);
		cargoS.save(cDesenvolvedor);
		funcionarioS.saveAll(Arrays.asList(funf1, funf2, funf3, funf4, funj1, funj2, funj3, funj4));
		
		instanciaItem(false);
		instanciaMovimentacao(false);
		unidadeS.save(un1);
		unidadeS.save(un2);

		itemS.saveAll(Arrays.asList(s1, s2, s3, s4, p1, p2, p3, p4, p5, p6, p7));
		movimentacaoS.saveAll(Arrays.asList(ent1, ent2, ent3, sai1, sai2, sai3));
		
		instanciaUsuarios();

		usuarioService.saveAll(Arrays.asList(user1,user2,user3,user4,user5));
		return this;
		
		
	}

	public PopulaBD instanciaPessoa() {
		estadoGO = new Estado(null, "GO", "Goias");
		goiania = new Cidade(null, "Goiania", estadoGO);
		endereco1 = new Endereco(null, "Test rua tal", "Centro", goiania, "123", "prox ao carai", "12345678",
				true, "Endereco residencial");
		pf1 = new PessoaFisica(null, "Joao Da Silva", "56661050004", LocalDate.of(1990, 4, 30),
				new RG("23123", "SSP", estadoGO), "Brasileira", goiania);
		pj1 = new PessoaJuridica(null, "Lanches", "99787331000180", "Lanches ME", "inscricaoEstadual1",
				"inscricaoMunicipal1");
		pf2 = new PessoaFisica(null, "Joao Snow", "52935546032", LocalDate.of(1995, 3, 30),
				new RG("3234", "SSP", estadoGO), "Brasileira", goiania);
		pf3 = new PessoaFisica(null, "Maria Silva", "07952632019", LocalDate.of(1980, 4, 30),
				new RG("54345", "SSP", estadoGO), "Brasileira", goiania);
		pf4 = new PessoaFisica(null, "Maria Carvalho", "58522943060", LocalDate.of(1990, 1, 30),
				new RG("4523", "SSP", estadoGO), "Brasileira", goiania);
		pj2 = new PessoaJuridica(null, "Juniscleids ME", "18038642000145", "Juniscleids ME", "inscricaoEstadual2",
				"inscricaoMunicipal2");
		pj3 = new PessoaJuridica(null, "Joao Dev", "46530490000139", "Profissionais ME", "inscricaoEstadual3",
				"inscricaoEstadual3");
		pj4 = new PessoaJuridica(null, "Mercado ME", "84912087000163", "Mercado ME", "inscricaoEstadual4",
				"inscricaoMunicipal4");

		pf5 = new PessoaFisica(null, "HEY", "05909561162", LocalDate.of(1994, 3, 30), new RG("34", "ssp", estadoGO),
				"Brasileira", goiania);
		return this;
	}

	public PopulaBD associaPessoa() {
		Arrays.asList(pf1, pj1, pf2, pf3, pf4, pf5, pj2, pj3, pj4).forEach(p -> {
			p.getEndereco().add(endereco1);
			p.getTelefone().add(Utils.getRandomTelefone(true));
			p.getEmail().add(Utils.getRandomEmail(p, true));
		});
		return this;
	}

	public PopulaBD instanciaFuncionario(boolean instanciaAssocia) {
		if (instanciaAssocia) {
			instanciaPessoa();
			associaPessoa();
		}
		instanciaCargos();
		funf1 = new Funcionario(null, pf1, "dev-432", cDesenvolvedor, LocalDate.now().minusWeeks(1), 0.,
				cDesenvolvedor.getSalarioBase());
		funj1 = new Funcionario(null, pj1, "adm-01", cAdministrador, LocalDate.now().minusDays(2), 0.1,
				cAdministrador.getSalarioBase());
		funf2 = new Funcionario(null, pf2, "dev-02", cDesenvolvedor, LocalDate.now().minusYears(1), 0.,
				cDesenvolvedor.getSalarioBase());
		funf3 = new Funcionario(null, pf3, "dev-03", cDesenvolvedor, LocalDate.now().minusYears(1), 0.,
				cDesenvolvedor.getSalarioBase());
		funf4 = new Funcionario(null, pf4, "dev-04", cDesenvolvedor, LocalDate.now().minusYears(1), 0.,
				cDesenvolvedor.getSalarioBase());

		funj2 = new Funcionario(null, pj2, "adm-02", cAdministrador, LocalDate.now().minusYears(1), 0.,
				cAdministrador.getSalarioBase());
		funj3 = new Funcionario(null, pj3, "adm-03", cAdministrador, LocalDate.now().minusYears(1), 0.,
				cAdministrador.getSalarioBase());
		funj4 = new Funcionario(null, pj4, "adm-04", cAdministrador, LocalDate.now().minusYears(1), 0.,
				cAdministrador.getSalarioBase());
		return this;
	}

	private PopulaBD instanciaCargos() {
		cDesenvolvedor = new Cargo(null, "Desenvolvedor", BigDecimal.valueOf(2000), "rsats");
		cAdministrador = new Cargo(null, "Administrador", BigDecimal.valueOf(5000), "tes");
		return this;
	}

	public PopulaBD instanciaCliente(boolean instanciaAssocia) {
		if (instanciaAssocia) {
			instanciaPessoa();
			associaPessoa();
		}
		cf1 = new Cliente(null, pf1, BigDecimal.valueOf(3000), "Cliente tal");
		cj1 = new Cliente(null, pj1, BigDecimal.valueOf(20000), "Empresa tal, cliente desde 1231");
		cf2 = new Cliente(null, pf2, BigDecimal.valueOf(1233), "12312");
		cf3 = new Cliente(null, pf3, BigDecimal.valueOf(1233), "12312");
		cf4 = new Cliente(null, pf4, BigDecimal.valueOf(2412), "descricao");
		cj2 = new Cliente(null, pj2, new BigDecimal(2133), "sdaf");
		cj3 = new Cliente(null, pj3, BigDecimal.valueOf(1233), "12312");
		cj4 = new Cliente(null, pj4, BigDecimal.valueOf(1233), "12312");
		return this;
	}

	public PopulaBD instanciaCategorias() {
		cat1 = new CategoriaItem(null, "Informatica");
		cat3 = new CategoriaItem(null, "Cama");
		cat2 = new CategoriaItem(null, "Eletrodomesticos");
		cat4 = new CategoriaItem(null, "Banho");
		return this;
	}

	public PopulaBD instanciaFornecedores(boolean instanciaAssocia) {
		if (instanciaAssocia) {
			instanciaPessoa();
			associaPessoa();
			instanciaCategorias();
		}

		forf1 = new Fornecedor(null, pf1);
		forf1.getCategorias().addAll(new HashSet<>(Arrays.asList(cat3, cat4)));
		forj1 = new Fornecedor(null, pj1);
		forj1.getCategorias().addAll(new HashSet<>(Arrays.asList(cat1, cat2)));
		forf2 = new Fornecedor(null, pf2);
		forf3 = new Fornecedor(null, pf3);
		forf4 = new Fornecedor(null, pf4);
		forj2 = new Fornecedor(null, pj2);
		forj3 = new Fornecedor(null, pj3);
		forj4 = new Fornecedor(null, pj4);
		forf5 = new Fornecedor(null, pf5);
		Arrays.asList(forf3, forf4).forEach(f -> f.getCategorias().addAll(Arrays.asList(cat3)));
		Arrays.asList(forj3, forj4, forf5).forEach(f -> f.getCategorias().addAll(Arrays.asList(cat1)));
		return this;
	}

	public PopulaBD instanciaItem(boolean instanciaAssocia) {
		if (instanciaAssocia) {
			instanciaCategorias();
		}

		instanciaUnidade();

		p1 = new Produto(null, "Computador", "Computador i5 8gbRam", un1, cat1, "COM01", BigDecimal.valueOf(100), null,
				0.3, 0.01, 5, 0, 10);
		s1 = new Servico(null, "Limpeza Cooler", "Limpesa cooler", un2, cat2, "LIMP02", BigDecimal.valueOf(100),
				BigDecimal.valueOf(20), 0.5, 0.02);

		p2 = Produto.of("Teclado Mecanico RGB", un1, cat3);
		
		p3 = Produto.of("Teclado membrana", un1, cat3);
		p4 = Produto.of("Computador i3  4gbram", un1, cat3);
		p5 = Produto.of("Mouse", un1, cat1);
		p6 = Produto.of("Teclado", un1, cat1);
		p7 = Produto.of("Processador i5", un1, cat1);
		Arrays.asList(p2,p3,p4,p5,p6,p7).stream().forEach( p ->{
			p.setEstoque(2, 4, 10);
			p.setComissaoVenda(1d);
			p.setValorCompraMedio(BigDecimal.valueOf(100));
			p.setMargemLucro(0.4d);
		});
			
		s2 = Servico.of("Troca de fonte", un1, cat2);
		s3 = Servico.of("Montagem Computador", un2, cat1);
		s4 = Servico.of("Formatacao", un1, cat2);
		
		Arrays.asList(s2,s3,s4).stream().forEach( s -> {
			s.setComissaoVenda(1d);
			s.setValorCompraMedio(BigDecimal.valueOf(100));
			s.setMargemLucro(0.4d);
		});
		return this;
	}

	public void instanciaUnidade() {
		un1 = new Unidade(null, "un", "Unidade");
		un2 = new Unidade(null, "hrs", "Horas");
	}

	public PopulaBD instanciaMovimentacao(boolean instanciaAssocia) {
		if (instanciaAssocia) {
			instanciaItem(true);
		}

		ent1 = Movimentacao.novaEntrada();
		sai1 = Movimentacao.novaSaida();
		ent2 = Movimentacao.novaEntrada();
		ent3 = Movimentacao.novaEntrada();
		sai2 = Movimentacao.novaSaida();
		sai3 = Movimentacao.novaSaida();
		Arrays.asList(ent1, sai1)
				.forEach(m -> m.getProdutoMovimentacao()
						.addAll(Arrays.asList(p1, p2, p3).stream()
								.map(p -> new ProdutoMovimentacao(new ProdutoMovimentacaoPK((Produto) p, ent1),
										BigDecimal.valueOf(10), BigDecimal.valueOf(100), 5))
								.collect(Collectors.toSet())));

		Arrays.asList(ent2, ent3, sai2, sai3)
				.forEach(m -> m.getProdutoMovimentacao().add(new ProdutoMovimentacao(new ProdutoMovimentacaoPK(p4, m),
						BigDecimal.valueOf(0), BigDecimal.valueOf(10), 10)));

		Arrays.asList(ent2, sai2).forEach(m -> {
			m.getProdutoMovimentacao().add(new ProdutoMovimentacao(new ProdutoMovimentacaoPK(p5, m),
					BigDecimal.valueOf(0), BigDecimal.valueOf(10), 5));
		});

		Arrays.asList(ent3, sai3)
				.forEach(m -> m.getProdutoMovimentacao()
						.addAll(Arrays.asList(p6, p7).stream()
								.map(p -> new ProdutoMovimentacao(new ProdutoMovimentacaoPK(((Produto) p), m),
										BigDecimal.valueOf(0), BigDecimal.valueOf(10), 4))
								.collect(Collectors.toSet())));
		return this;

	}

	public PopulaBD instanciaUsuarios() {
		 
		user1 = new Usuario(null, "gustavo", "123456");
		user1.setFuncionario(funf1);
		PermissaoE.getPermissaoStream().filter(p -> p.getMod().equals(Modulo.PESSOAL)).forEach( p-> user1.addPermissao(p));
		
		
		user2 = new Usuario(null, "login2", "senha2");
		user2.setFuncionario(funf2);
		PermissaoE.getPermissaoStream().filter(p -> p.getNome().toLowerCase().contains("get")).forEach(p -> user2.addPermissao(p));
		
		user3 = new Usuario(null, "login3", "senha3");
		user3.setFuncionario(funf3);
		PermissaoE.getPermissaoStream().filter(p -> p.getMod().equals(Modulo.ESTOQUE)).forEach(p -> user3.addPermissao(p));
		
		user4 = new Usuario(null, "login4", "senha4");
		user4.setFuncionario(funf4);
		
		user5 = new Usuario(null, "adm", "adm");
		user5.setFuncionario(funj1);
		PermissaoE.getPermissaoStream().forEach(p -> user5.addPermissao(p));
		return this;
		
	}

	

}
