package com.ebm.pessoal.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Cidade;
import com.ebm.pessoal.domain.Endereco;
import com.ebm.pessoal.domain.Estado;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.repository.CidadeRepository;
import com.ebm.pessoal.repository.EnderecoRepository;
import com.ebm.pessoal.repository.EstadoRepository;

@Service
public class EnderecoService {
	
	public static final String DATAINTEGRITY_ENDERECOCIDADE = DataIntegrityException.DEFAULT+": É necessario uma cidade para salvar o endereco";
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private CidadeService cidadeService;
	
	
	public Endereco save(Endereco endereco) {
		
		if(endereco.getCidade() == null)
			throw new DataIntegrityException(DATAINTEGRITY_ENDERECOCIDADE);
		
		endereco.setCidade(cidadeService.save(endereco.getCidade()));
		
		return enderecoRepository.save(endereco);
	}	

	
	public List<Endereco> salveAll(List<Endereco> endereco) {
		return endereco.stream().map( e -> this.save(e)).collect(Collectors.toList());
	}

	
	//delete --------------------------------------------------------------------------------------------------------
	public void deleteById(Integer id) {
		find(id);
		enderecoRepository.deleteById(id);
	}
	public void deleteAll(List<Endereco> endereco) {
		endereco.forEach( e -> deleteById(e.getId()));
	}
	public void deleteAll() {
		enderecoRepository.deleteAll();

	}
	public void deleteByPessoaId(Integer id) {
		deleteAll(findByPessoaId(id));
	}
	
	
	//find --------------------------------------------------------------------------------------------------------
	public Endereco find(Integer id) {
		return enderecoRepository.findById(id).orElseThrow( 
				() -> new ObjectNotFoundException("Não foi possivel encontrar o endereco de id: " + id));
	}
	public List<Endereco> findBy(Pessoa pessoa) {
		return findByPessoaId(pessoa.getId());
		
	}
	public List<Endereco> findByPessoaId(Integer id) {
		List<Endereco> enderecos = enderecoRepository.findByPessoa(id);
		
		if(enderecos.size() == 0 || enderecos == null) 
			throw new ObjectNotFoundException("A pessoa de id: " + id  + " não possui endereco cadastrado");
		return enderecos;
	}
	public List<String> getTipoEndereco(){
		return enderecoRepository.findAllTipoEndereco();
	}



}


