package com.ebm.estoque.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.estoque.domain.CategoriaItem;
import com.ebm.estoque.domain.Fornecedor;
import com.ebm.estoque.dtos.FornecedorListDTO;
import com.ebm.estoque.repository.FornecedorRepository;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.FornecedorService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.service.PessoaService;

@Service
public class FornecedorServiceImpl implements FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private CategoriaItemService categoriaService;
	

	@Transactional
	public Fornecedor save(Fornecedor fornecedor) {
		garantaIntegridade(fornecedor);
		saveAssociations(fornecedor);
		return fornecedorRepository.save(fornecedor);
	}

	private void saveAssociations(Fornecedor fornecedor) {
		fornecedor.setPessoa(pessoaService.save(fornecedor.getPessoa()));
		Set<CategoriaItem> categorias = fornecedor.getCategorias();
		
		if(!categorias.isEmpty() && Optional.ofNullable(categorias).isPresent() )
			fornecedor.setCategorias(categoriaService.findAllById(categorias.stream().map(CategoriaItem::getId).collect(Collectors.toSet())));
	}

	@Override
	public List<Fornecedor> saveAll(List<Fornecedor> fornecedores) {
		return fornecedores.stream().map(f -> this.save(f)).collect(Collectors.toList());
	}

	private void garantaIntegridade(Fornecedor fornecedor) {
		if (fornecedor.getPessoa() == null)
			throw new DataIntegrityException(DATAINTEGRITY_FORNECEDORWITHOUTPERSON);

		if (fornecedor.getPessoa().getId() != null) {
			try {
				Fornecedor result = findById(fornecedor.getPessoa().getId());
				if (result.getId() != fornecedor.getId())
					throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEPERSON);
			} catch (ObjectNotFoundException ex) {}
		}

		if (fornecedor.getId() != null && fornecedor.getId() != fornecedor.getPessoa().getId())
			throw new DataIntegrityException(DATAINTEGRITY_CHANCEPERSON);
	}

	public Fornecedor findById(Integer id) {
		if (!Optional.ofNullable(id).isPresent())
			throw new DataIntegrityException(DATAINTEGRITY_IDNULL);
		return fornecedorRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(ONFE_BYID + id));
	}

	@Override
	public Fornecedor findByCpfOrCnpj(String document) {

		return findById(pessoaService.findByCpfOrCnpj(document).getId());
	}
	
	@Override
	public Page<FornecedorListDTO> findBy(TipoPessoa tipo, String nome, Set<Integer> categoriasId, PageRequest pageRequest) {

		ExampleMatcher matcher = PessoaService.ExampleMatcherDinamicFilterFor(true, tipo);

		Pessoa pessoa = PessoaService.getPessoa(tipo,nome);
	
		Fornecedor fornecedor = new Fornecedor(null,  pessoa);
		List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();
		if(  !(tipo!=null || nome != null || (categoriasId != null && !categoriasId.isEmpty()))) {
			
			if(tipo!=null || nome != null)
				fornecedores = fornecedorRepository.findAll(Example.of(fornecedor, matcher));
			
			if(categoriasId != null && !categoriasId.isEmpty()) {
				List<Fornecedor> fornecedoresCategoria = fornecedorRepository.findByCategoriasIn(categoriaService.findAllById(categoriasId));
				
				if(fornecedores.size() > 0) {
					fornecedores.retainAll(fornecedoresCategoria);
				}
					
				 else {
					 fornecedores.addAll(fornecedoresCategoria);
				 }
					
			}
		}
		else {
			List<FornecedorListDTO> fornecedoresl = fornecedorRepository.findAll(pageRequest).stream()
					.map(f-> new FornecedorListDTO(f)).collect(Collectors.toList());
			return new PageImpl<>(fornecedoresl, pageRequest, fornecedoresl.size()); 
		}

		
		List<FornecedorListDTO> fornecedorsDTO = fornecedores.stream().map(c -> new FornecedorListDTO(c)).collect(Collectors.toList());

		return new PageImpl<>(fornecedorsDTO, pageRequest, fornecedorsDTO.size());
	}

	@Override
	public void deleteAll() {
		fornecedorRepository.deleteAll();
	}

}
