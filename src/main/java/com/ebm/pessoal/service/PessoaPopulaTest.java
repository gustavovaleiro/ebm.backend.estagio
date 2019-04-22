package com.ebm.pessoal.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebm.pessoal.domain.Cargo;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Cliente;
import com.ebm.pessoal.domain.Email;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.PessoaFisica;
import com.ebm.pessoal.domain.PessoaJuridica;
import com.ebm.pessoal.domain.RG;
import com.ebm.pessoal.domain.Telefone;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.repository.CidadeRepository;
import com.ebm.pessoal.repository.ClienteRepository;
import com.ebm.pessoal.repository.EstadoRepository;

@Service
public class PessoaPopulaTest {
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
    private ClienteService clienteService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EstadoRepository estado;
	
	@Autowired 
	private CidadeService cidade;
	
	
	private Estado estadoGO;
	private Estado estadoMT;
	private Cidade cuiaba;
	private Cidade goiania;
	private Endereco endereco1;
	private Endereco endereco2;
	private Endereco endereco3;

	
	
	private Telefone telefone1;
	private Telefone telefone2;

	private Email email2;
	private Email email1;

	
	private PessoaFisica pf1;
	private PessoaFisica pf2;
	private PessoaFisica pf3;
	private PessoaFisica pf4;
	private PessoaJuridica pj1;
	private PessoaJuridica pj4;
	private PessoaJuridica pj2;
	private PessoaJuridica pj3;

	private Cliente cli1;
	private Cliente cli2;
	private Cliente cli3;
	private Cliente cli4;
	private Cliente cli5;
	private Cliente cli6;

	private Cargo cargo1;
	private Cargo cargo2;


	private Funcionario funcionario1;
	private Funcionario funcionario2;
	private Funcionario funcionario3;
	private Funcionario funcionario4;
	private Funcionario funcionario5;
	private Funcionario funcionario6;

	
	//Instaciar Pessoas
	public void InstanciaPessoas() {
		instanciarAssociacoesPessoa();

		
		
		pf1 = new PessoaFisica(null, "Joao Da Silva", "56661050004", LocalDate.of(1990, 4, 30), new RG("23123", "SSP", estadoGO ), "Brasileira", goiania);
		pf2 = new PessoaFisica(null, "Joao Snow", "52935546032", LocalDate.of(1995, 3, 30), new RG("3234", "SSP", estadoGO ), "Brasileira", goiania);
		pf3 = new PessoaFisica(null, "Maria Silva", "07952632019", LocalDate.of(1980, 4, 30), new RG("54345", "SSP", estadoGO ), "Brasileira", cuiaba);
		pf4 = new PessoaFisica(null, "Maria Carvalho", "58522943060", LocalDate.of(1990, 1, 30), new RG("4523", "SSP", estadoGO ), "Brasileira", cuiaba);
		pj1 = new PessoaJuridica(null, "Lanches", "99787331000180", "Lanches ME", "inscricaoEstadual1", "inscricaoMunicipal1");
		pj2 = new PessoaJuridica(null, "Juniscleids ME", "18038642000145", "Juniscleids ME", "inscricaoEstadual2", "inscricaoMunicipal2");
		pj3 = new PessoaJuridica(null, "Profissionais", "46530490000139", "Profissionais ME", "inscricaoEstadual3", "inscricaoEstadual3");
		pj4 = new PessoaJuridica(null, "Mercado ME", "84912087000163", "Mercado ME", "inscricaoEstadual4", "inscricaoMunicipal4");
		
	}
	public  List< Pessoa> getPessoas(TipoPessoa tipo){
	
		List<Pessoa> pessoas = new ArrayList<>();
		if(tipo == TipoPessoa.ALL || tipo == TipoPessoa.PESSOAFISICA) {
			pessoas.addAll(Arrays.asList(pf1,pf2,pf3,pf4));
		} 
		if(tipo == TipoPessoa.ALL || tipo == TipoPessoa.PESSOAJURIDICA) {
			pessoas.addAll(Arrays.asList(pj1,pj2,pj3,pj4));
		}
		return pessoas;
	}
	

	public void instanciarAssociacoesPessoa() {
		estadoGO = new Estado(null, "GO", "Goias");
		estadoMT = new Estado(null, "MT", "Mato Grosso");
		cuiaba = new Cidade(null, "Cuiaba", estadoMT);
		goiania = new Cidade(null, "Goiania", estadoGO);
		endereco1 = new Endereco(null, "Test rua tal", "Centro", cuiaba, "123", "prox ao carai", "12345678", "Endereco residencial");
		endereco2 = new Endereco(null, "Outrao rua", "bairro qualquer", cuiaba, "43", "prox tal", "12345678", "Endereco profissional");
		endereco3 = new Endereco(null, "Rual tal", "Industrial", goiania, "34", "", "", "");

		telefone1 = new Telefone(null, "33", "34234234", "Residencial");
		telefone2 = new Telefone(null, "66", "12241123", "Celular");

		email2 = new Email("test@gmail.com", "");
		email1 = new Email("tes43t@gmail.com", "");
	}
	
	public void InstanciarCliente() {
		cli1 = new Cliente(null, pf1, new BigDecimal(2133), "sdaf");
		cli2 = new Cliente(null, pf2, BigDecimal.valueOf(1233), "12312");
		cli3 = new Cliente(null, pj1, BigDecimal.valueOf(2412), "descricao");
		cli4 = new Cliente(null, pj2, new BigDecimal(2133), "sdaf");
		cli5 = new Cliente(null, pj3, BigDecimal.valueOf(1233), "12312");
		cli6 = new Cliente(null, pj4, BigDecimal.valueOf(2412), "descricao");
	}
	
	public void InstanciarFuncionarios() {
		cargo1 = new Cargo(null, "Vendeddor", BigDecimal.valueOf(1000));
		cargo2 = new Cargo(null, "Administrador", BigDecimal.valueOf(2000));
		
		
		funcionario1 = new Funcionario(null, pf1, "43", cargo1, LocalDate.now(), 0.2, BigDecimal.valueOf(100));
		funcionario2 = new Funcionario(null, pf2, "434", cargo2, LocalDate.now(), 0, BigDecimal.valueOf(1000));
		funcionario3 = new Funcionario(null, pf3, "43", cargo1, LocalDate.now(), 0.2, BigDecimal.valueOf(100));
		funcionario4 = new Funcionario(null, pj1, "434", cargo2, LocalDate.now(), 0, BigDecimal.valueOf(1000));
		funcionario5 = new Funcionario(null, pj4, "43", cargo1, LocalDate.now(), 0.2, BigDecimal.valueOf(100));
		funcionario6 = new Funcionario(null, pj3, "434", cargo2, LocalDate.now(), 0, BigDecimal.valueOf(1000));
	}


	public PessoaFisica getPf1() {
		return pf1;
	}

	public void setPf1(PessoaFisica pf1) {
		this.pf1 = pf1;
	}

	public PessoaFisica getPf2() {
		return pf2;
	}

	public void setPf2(PessoaFisica pf2) {
		this.pf2 = pf2;
	}
	

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}

	public EstadoRepository getEstado() {
		return estado;
	}

	public CidadeService getCidade() {
		return cidade;
	}

	public Estado getEstadoGO() {
		return estadoGO;
	}

	public Estado getEstadoMT() {
		return estadoMT;
	}

	public Cidade getCuiaba() {
		return cuiaba;
	}

	public Cidade getGoiania() {
		return goiania;
	}

	public Endereco getEndereco1() {
		return endereco1;
	}

	public Endereco getEndereco2() {
		return endereco2;
	}

	public Endereco getEndereco3() {
		return endereco3;
	}

	public Telefone getTelefone1() {
		return telefone1;
	}

	public Telefone getTelefone2() {
		return telefone2;
	}

	public Email getEmail2() {
		return email2;
	}

	public Email getEmail1() {
		return email1;
	}

	public Cargo getCargo1() {
		return cargo1;
	}

	public Cargo getCargo2() {
		return cargo2;
	}

	public PessoaFisica getPf3() {
		return pf3;
	}

	public void setPf3(PessoaFisica pf3) {
		this.pf3 = pf3;
	}

	public PessoaFisica getPf4() {
		return pf4;
	}

	public void setPf4(PessoaFisica pf4) {
		this.pf4 = pf4;
	}

	public PessoaJuridica getPj1() {
		return pj1;
	}

	public void setPj1(PessoaJuridica pj1) {
		this.pj1 = pj1;
	}

	public PessoaJuridica getPj4() {
		return pj4;
	}

	public void setPj4(PessoaJuridica pj4) {
		this.pj4 = pj4;
	}

	public PessoaJuridica getPj2() {
		return pj2;
	}

	public void setPj2(PessoaJuridica pj2) {
		this.pj2 = pj2;
	}

	public PessoaJuridica getPj3() {
		return pj3;
	}

	public void setPj3(PessoaJuridica pj3) {
		this.pj3 = pj3;
	}

	public Cliente getCli1() {
		return cli1;
	}

	public void setCli1(Cliente cli1) {
		this.cli1 = cli1;
	}

	public Cliente getCli2() {
		return cli2;
	}

	public void setCli2(Cliente cli2) {
		this.cli2 = cli2;
	}

	public Cliente getCli3() {
		return cli3;
	}

	public void setCli3(Cliente cli3) {
		this.cli3 = cli3;
	}

	public Cliente getCli4() {
		return cli4;
	}

	public void setCli4(Cliente cli4) {
		this.cli4 = cli4;
	}

	public Cliente getCli5() {
		return cli5;
	}

	public void setCli5(Cliente cli5) {
		this.cli5 = cli5;
	}

	public Cliente getCli6() {
		return cli6;
	}

	public void setCli6(Cliente cli6) {
		this.cli6 = cli6;
	}

	public Funcionario getFuncionario1() {
		return funcionario1;
	}

	public void setFuncionario1(Funcionario funcionario1) {
		this.funcionario1 = funcionario1;
	}

	public Funcionario getFuncionario2() {
		return funcionario2;
	}

	public void setFuncionario2(Funcionario funcionario2) {
		this.funcionario2 = funcionario2;
	}

	public Funcionario getFuncionario3() {
		return funcionario3;
	}

	public void setFuncionario3(Funcionario funcionario3) {
		this.funcionario3 = funcionario3;
	}

	public Funcionario getFuncionario4() {
		return funcionario4;
	}

	public void setFuncionario4(Funcionario funcionario4) {
		this.funcionario4 = funcionario4;
	}

	public Funcionario getFuncionario5() {
		return funcionario5;
	}

	public void setFuncionario5(Funcionario funcionario5) {
		this.funcionario5 = funcionario5;
	}

	public Funcionario getFuncionario6() {
		return funcionario6;
	}

	public void setFuncionario6(Funcionario funcionario6) {
		this.funcionario6 = funcionario6;
	}
	
	public void cleaAll() {
		clienteService.deleteAll();
		pessoaService.deleteAll(true);
		cidade.deleteAll(true);
	}
}
