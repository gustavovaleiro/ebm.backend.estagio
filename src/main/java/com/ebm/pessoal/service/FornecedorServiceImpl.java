package com.ebm.pessoal.service;


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
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;
import com.ebm.geral.utils.Utils;
import com.ebm.pessoal.domain.Fornecedor;
import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.TipoPessoa;
import com.ebm.pessoal.dtos.FornecedorListDTO;
import com.ebm.pessoal.repository.FornecedorRepository;
import com.ebm.pessoal.service.interfaces.FornecedorService;

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
		Utils.audita(fornecedor.getHistorico());
		return fornecedorRepository.save(fornecedor);
	}

	private void saveAssociations(Fornecedor fornecedor) {
		fornecedor.setPessoa(pessoaService.findById(fornecedor.getPessoa().getId()));
		Set<CategoriaItem> categorias = fornecedor.getCategorias();
		
		if(Optional.ofNullable(categorias).isPresent() && !categorias.isEmpty()  )
			fornecedor.setCategorias(categoriaService.findAllById(categorias.stream().map(CategoriaItem::getId).collect(Collectors.toSet())));
	}

	@Override
	public List<Fornecedor> saveAll(List<Fornecedor> fornecedores) {
		return fornecedores.stream().map(f -> this.save(f)).collect(Collectors.toList());
	}

	private void garantaIntegridade(Fornecedor fornecedor) {
		if (fornecedor.getPessoa() == null)
			throw new DataIntegrityException(DATAINTEGRITY_FORNECEDORWITHOUTPERSON);
		try {
			Fornecedor pessoaWithDocument = this.findByCpfOrCnpj(fornecedor.getPessoa().getDocument());
			if (!pessoaWithDocument.getId().equals(fornecedor.getId()))
				throw new DataIntegrityException(DATAINTEGRITY_CHANCEPERSON );
		}catch( ObjectNotFoundException ex) {
			
		}
		if (fornecedor.getPessoa().getId() != null) {
			try {
				Fornecedor result = findById(fornecedor.getPessoa().getId());
				if (!result.getId().equals(fornecedor.getId()))
					throw new DataIntegrityException(DATAINTEGRITY_DUPLICATEPERSON);
			} catch (ObjectNotFoundException ex) {}
		}

		if (fornecedor.getId() != null && !fornecedor.getId().equals(fornecedor.getPessoa().getId()))
			throw new DataIntegrityException(DATAINTEGRITY_CHANCEPERSON );
	}

	public Fornecedor findById(Integer id) {
		if (!Optional.ofNullable(id).isPresent())
			throw new DataIntegrityException(DATAINTEGRITY_IDNULL);
		return fornecedorRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(ONFE_BYID + id));
	}

	@Override
	public Fornecedor findByCpfOrCnpj(String document) {
		try {
			return findById(pessoaService.findByCpfOrCnpj(document).getId());
		} catch(ObjectNotFoundException ex) {
			throw new ObjectNotFoundException(PessoaService.NOT_FOUND_DOCUMENT + document);
		}
		
	}
	
	@Override
	public Page<FornecedorListDTO> findBy(TipoPessoa tipo, String nome, Set<Integer> categoriasId, PageRequest pageRequest) {

		ExampleMatcher matcher = PessoaService.ExampleMatcherDinamicFilterFor(true, tipo);

		Pessoa pessoa = PessoaService.getPessoa(tipo,nome);
	
		Fornecedor fornecedor = new Fornecedor(null,  pessoa);
		List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();
		if(  (tipo!=null || nome != null || (categoriasId != null && !categoriasId.isEmpty()))) {
			
			if(tipo!=null || nome != null)
				fornecedores = fornecedorRepository.findAll(Example.of(fornecedor, matcher));
			
			if(categoriasId != null && !categoriasId.isEmpty()) {
				Set<CategoriaItem> cats = categoriaService.findAllById(categoriasId);
				
				if(fornecedores.size() > 0) {
					fornecedores.retainAll(fornecedorRepository.findDistinctByCategoriasIn(cats));
				}
				 else {
					 
					 fornecedores =  fornecedorRepository.findDistinctByCategoriasIn(cats,  pageRequest).getContent();
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

	@Transactional
	@Override
	public void delete(Integer id) {
		
		if(id == null)
			throw new DataIntegrityException(FornecedorService.DATAINTEGRITY_IDNULL);
		Fornecedor result = findById(id);
		// verificar se existe alguma entra na movimentação associada a ele , caso existe nao deixar escluir
		
		fornecedorRepository.delete(result);
	}
	@Override
	public void deleteAll() {
		fornecedorRepository.deleteAll();
	}

}
