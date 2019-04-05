package com.ebm.pessoal.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

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
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	
	
	//insert --------------------------------------------------------------------------------------------------------
	public Endereco insert(Endereco endereco) {
		endereco.setId(null);
		
		lidaComEstado(endereco.getCidade());
		
		Optional<Cidade> cidadeResult = cidadeRepository.findOneByNomeAndEstado(endereco.getCidade().getNome(), endereco.getCidade().getEstado().getUF());
		if(!cidadeResult.isPresent())
			endereco.setCidade(insert(endereco.getCidade()));
		else {
			endereco.setCidade(cidadeResult.get());
		}
			
			
		return enderecoRepository.save(endereco);
	}

	public void lidaComEstado(Cidade cidade) {
		Optional<Estado> estadoResult = estadoRepository.findOneByUF(cidade.getEstado().getUF());
		if(!estadoResult.isPresent()) 
			cidade.setEstado(estadoRepository.save(cidade.getEstado()));
		else
			cidade.setEstado(estadoResult.get());
	}
	
	public Cidade insert(Cidade cidade) {
		cidade.setId(null);
		
		return lidaComCidadeInsert(cidade);
	}

	
	
	public List<Endereco> insertAll(List<Endereco> endereco) {
		return endereco.stream().map( e -> this.insert(e)).collect(Collectors.toList());
	}
		
	 
	//update --------------------------------------------------------------------------------------------------------
	public Endereco update(Endereco endereco) {
		find(endereco.getId());
		return enderecoRepository.save(endereco);
	}
	public List<Endereco> updateAll(List<Endereco> endereco) {
		return endereco.stream().map( e -> this.update(e)).collect(Collectors.toList());
	}
	
	public Cidade update(Cidade cidade) {
		 findCidadeById(cidade.getId());
		 return cidadeRepository.save(cidade);
	}
	public Estado update(Estado estado) {
		 findCidadeById(estado.getId());
		 return estadoRepository.save(estado);
	}
		
	
	//delete --------------------------------------------------------------------------------------------------------
	public void deleteById(Integer id) {
		find(id);
		enderecoRepository.deleteById(id);
	}
	public void deleteAll(List<Endereco> endereco) {
		endereco.forEach( e -> deleteById(e.getId()));
	}
	public void deleteByPessoaId(Integer id) {
		deleteAll(findByPessoaId(id));
	}
	
	public void deleteCidadeById(Integer id) {
		cidadeRepository.delete(findCidadeById(id));
	}
		
	public void deleteEstadoById(Integer id) {
		estadoRepository.delete(findEstadoById(id));
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
	

	public Cidade findCidadeById(Integer id) {
		Optional<Cidade> cidadeBd = cidadeRepository.findById(id);
		return cidadeBd.orElseThrow(
				() -> new ObjectNotFoundException("Cidade de id: " + id + " não pode ser encontrada"));
	}
	public Estado findEstadoById(Integer id) {
		Optional<Estado> estado = estadoRepository.findById(id);
		return estado.orElseThrow(
				() -> new ObjectNotFoundException("Estado de id: " + id + " não pode ser encontradO"));
	}
	
	public List<Cidade> findCidadeByEstado(Estado estado){
		return cidadeRepository.findAllByEstado(estado);
	}
	public List<Cidade> findCidadeByEstadoId(Integer id){
		return cidadeRepository.findAllByEstado(findEstadoById(id));
	}
	public Optional<Cidade> findCidadeByNomeAndEstado(String nome, String uf) {
		// TODO Auto-generated method stub
		return cidadeRepository.findOneByNomeAndEstado(nome, uf);
	}
	
	public boolean exist(Cidade cidade) {
		return cidadeRepository.exists(Example.of(cidade));
	}


	public Cidade lidaComCidadeInsert(Cidade cidade) {
		Optional<Cidade> cidadeResult = cidadeRepository.findOneByNomeAndEstado(cidade.getNome(),cidade.getEstado().getUF());
		if(!cidadeResult.isPresent()) {
			lidaComEstado(cidade);
			return cidadeRepository.save(cidade);
		}
		else {
			return cidadeResult.get();
		}
	}
	
	

}


