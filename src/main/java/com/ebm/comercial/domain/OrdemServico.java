package com.ebm.comercial.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ebm.auth.Usuario;
import com.ebm.estoque.domain.Servico;
import com.ebm.pessoal.domain.Cliente;

@Entity
public class OrdemServico extends Venda {

	private static final long serialVersionUID = 1L;
	
	
	private LocalDate dataTerminoEfetiva;
	private LocalDate dataTerminoPrevista;
	@OneToOne
	private AvaliacaoServico avaliacao;
	
	
	private Integer estadoC;
	
	@OneToMany
	private Set<ServicoOrdem> servicosOrdem = new HashSet<ServicoOrdem>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<FuncionarioFuncao> funcionario;
	private boolean atraso;
	private OSEstado estado;
	public OrdemServico() {}

	public OrdemServico(Integer id, LocalDate dataAbertura, BigDecimal descontoGeral, Cliente cliente,
			Usuario autorizouDesconto, String observacao,LocalDate dataTermino, OSEstado estado) {
		super(id, dataAbertura, descontoGeral, cliente, autorizouDesconto, observacao);
		this.dataTerminoPrevista = dataTermino;
		this.estadoC = estado.getCod();
		this.estado = estado;
	
	}
	
	public OSEstado getEstadoAtual() {
		return OSEstado.toEnum(estadoC);
	}
	
	public LocalDate getDataTerminoPrevista() {
		return dataTerminoPrevista;
	}
	
	public LocalDate getDataTerminoEfetiva() {
		return dataTerminoEfetiva;
	}

	public void setDataTerminoEfetiva(LocalDate dataTerminoEfetiva) {
		this.dataTerminoEfetiva = dataTerminoEfetiva;
	}

	public void setDataTerminoPrevista(LocalDate dataTermino) {
		this.dataTerminoPrevista = dataTermino;
	}

	public AvaliacaoServico getAvaliacao() {
		return avaliacao;
	}
	
	public void setAvaliacao(AvaliacaoServico avaliacao) {
		this.avaliacao = avaliacao;
	}

	public OSEstadoOperations getEstado() {
		return estado;
	}
	public Set<ServicoOrdem> getServicosOrdem() {
		return servicosOrdem;
	}
	
	public Set<Servico> getServicos(){
		return this.servicosOrdem.stream().map(ServicoOrdem::getServico).collect(Collectors.toSet());
	}
	
	public void setServicosOrdem(Set<ServicoOrdem> servicos) {
		this.servicosOrdem = servicos;
	}

	public List<FuncionarioFuncao> getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(List<FuncionarioFuncao> funcionario) {
		this.funcionario = funcionario;
	}

	public void aprovar() {
		setEstado(estado.aprovar(this));
		
	}

	public void cancelar() {
		setEstado(estado.cancelar(this));
	}

	public void finalizar() {
		setEstado(estado.finalizar(this));
	}

	public void produzir() {
		setEstado(estado.produzir(this));
	}

	public void emAtraso() {
		estado.emAtraso(this);
	}

	public String getEstadoName() {
		return estado.getEstadoName();
	}
	
	public double getValorTotalServicos() {
		return this.servicosOrdem.stream().mapToDouble(s -> s.getSubTotal()).sum();
	}
	public double getValorTotalServicos() {
		double total = 0;
		for(ServicoOrdem servico: this.servicosOrdem) {
			total+=servico.getSubTotal();
		}
		return total;
	}
		
	public double getValorTotal() {
		return super.getValorTotal() + this.getValorTotalServicos();
	}
	public double getLucroServico() {
		return this.servicosOrdem.stream().mapToDouble(x -> x.getLucro()).sum();
	}
	public double getLucroTotal() {
		return super.getLucroProdutos() + this.getLucroServico();
	}
	

	public boolean isAtraso() {
		return atraso;
	}

	public void setAtraso(boolean atraso) {
		this.atraso = atraso;
	}

	private void setEstado(OSEstado estado) {
		this.estado = estado;
		this.estadoC = estado.getCod();
	}
}
