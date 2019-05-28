package com.ebm.pessoal.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.ebm.Utils;
import com.ebm.estoque.domain.CategoriaItem;
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

@Service
public class PessoalPopulaBD {
	
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

	public void instanciaPessoa(){	
		estadoGO = new Estado(null, "GO", "Goias");
		goiania = new Cidade(null, "Goiania", estadoGO);
		endereco1 = new Endereco(null, "Test rua tal", "Centro", goiania, "123", "prox ao carai", "12345678",
				"Endereco residencial", true);
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
	    pj2 = new PessoaJuridica(null, "Juniscleids ME", "18038642000145", "Juniscleids ME",
				"inscricaoEstadual2", "inscricaoMunicipal2");
	    pj3 = new PessoaJuridica(null, "Joao Dev", "46530490000139", "Profissionais ME",
				"inscricaoEstadual3", "inscricaoEstadual3");
	    pj4 = new PessoaJuridica(null, "Mercado ME", "84912087000163", "Mercado ME",
				"inscricaoEstadual4", "inscricaoMunicipal4");
		
	     pf5 = new PessoaFisica(null, "HEY", "05909561162", LocalDate.of(1994, 3, 30),
				new RG("34", "ssp", estadoGO), "Brasileira", goiania);
	}
	public void associaPessoa() {
		Arrays.asList(pf1,pj1,pf2, pf3, pf4, pf5, pj2, pj3, pj4).forEach(p -> {
			p.getEndereco().add(endereco1);
			p.getTelefone().add(Utils.getRandomTelefone(true));
			p.getEmail().add(Utils.getRandomEmail(p,true));
		});
	}
	public void instanciaFuncionario(boolean instanciaAssocia) {
		if(instanciaAssocia) {
			instanciaPessoa();
			associaPessoa();
		}
		cDesenvolvedor = new Cargo(null, "Desenvolvedor", BigDecimal.valueOf(2000), "rsats");
		cAdministrador = new Cargo(null, "Administrador", BigDecimal.valueOf(5000), "tes");
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
	}
	
	public void instanciaCliente(boolean instanciaAssocia) {
		if(instanciaAssocia) {
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
	}
	public void instanciaFornecedores(boolean instanciaAssocia) {
		if(instanciaAssocia) {
			instanciaPessoa();
			associaPessoa();
		}
		cat1 = new CategoriaItem(null, "Informatica");
		cat3 = new CategoriaItem(null, "Cama");
		cat2 = new CategoriaItem(null, "Eletrodomesticos");
		cat4 = new CategoriaItem(null, "Banho");
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
	}
}
