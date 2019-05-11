package com.ebm.estoque.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.Utils;
import com.ebm.estoque.domain.Item;
import com.ebm.estoque.domain.enums.TipoItem;
import com.ebm.estoque.dtos.ItemListDTO;
import com.ebm.estoque.repository.ItemRepository;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private UnidadeService unidadeService;
	@Autowired
	private CategoriaItemService categoriaService;
	@Override
	public void deleteAll(boolean b) {
		itemRepository.deleteAll();
		if(b) {
			unidadeService.deleteAll();
			categoriaService.deleteAll();
		}
		
	}

	@Override
	public Item save(Item item) {
		garantaIntegridade(item);
		salvaAssociacao(item);
		if(item.getId() == null) {
			item.setDataCadastro(LocalDateTime.now());
		}else {item.setDataUltimaModificacao(LocalDateTime.now());}
		
		return itemRepository.save(item);
	}

	private void garantaIntegridade(Item item) {
		if(item.getUnidade() == null) 
			throw new DataIntegrityException(DATAINTEGRITY_UNIDADENULL);
		if(item.getCategoria() == null)
			throw new DataIntegrityException(DATAINTEGRITY_CATEGORIANULL);

	}

	private void salvaAssociacao(Item item) {
		item.setUnidade(unidadeService.save(item.getUnidade()));
		item.setCategoria(categoriaService.save(item.getCategoria()));
	}
	

	@Override
	public void delete(Integer id) {
		if(id==null)
			throw new DataIntegrityException(DATAINTEGRITY_IDNULL);
		
		//verificar se existe uma movimentacao no estoque, venda, ordem de sevico
		itemRepository.deleteById(id);
	}

	@Override
	public Item findById(Integer id) {
		if(!Optional.ofNullable(id).isPresent())
			throw new DataIntegrityException(DATAINTEGRITY_IDNULL);
		return itemRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(ONFE_BYID));
	}

	
	
	@Override
	public List<Item> findBy(String codigoInterno, String tipo, String nome, String unidade, String categoria) {
		ExampleMatcher matcher = Utils.getExampleMatcherForDinamicFilter(true);

		List<Item> itens = new ArrayList<>();

		if(tipo == null || tipo.isEmpty()) {
			matcher = matcher.withIgnorePaths("tipo");
			preparaItens(codigoInterno, nome, unidade, categoria, matcher, itens, TipoItem.PRODUTO.getInstance());
			preparaItens(codigoInterno, nome, unidade, categoria, matcher, itens, TipoItem.SERVICO.getInstance());	
		}else 
			preparaItens(codigoInterno, nome, unidade, categoria, matcher, itens, TipoItem.fromString(tipo).getInstance());
		
		return itens;
	}
	
	@Override
	public Page<ItemListDTO> findBy(String codigoInterno, String tipo, String nome, String unidade, String categoria,
			PageRequest pageRequest) {
		List<ItemListDTO> itens = findBy(codigoInterno, tipo, nome, unidade, categoria).stream().map( i -> new ItemListDTO(i)).collect(Collectors.toList());
		
		return new PageImpl<>(itens, pageRequest, itens.size());
		
	}
	private void preparaItens(String codigoInterno, String nome, String unidade, String categoria,
			ExampleMatcher matcher, List<Item> itens, Item item) {
		item.setCodInterno(codigoInterno);
		if(Optional.ofNullable(categoria).isPresent())
			item.setCategoria(categoriaService.findByNome(categoria));
		if(Optional.ofNullable(unidade).isPresent())
			item.setUnidade(unidadeService.findByAbrev(unidade));
		item.setNome(nome);
		
		itens.addAll(itemRepository.findAll(Example.of(item, matcher)));
	}
	@Override
	public Double calcularComissaoEstimada(Collection<Item> item, Double taxaComissaoA) {
		BigDecimal taxaComissao = BigDecimal.valueOf(Optional.of(taxaComissaoA).orElse(0d));
		return item.stream().mapToDouble(i -> i.getPrecoVenda().multiply(taxaComissao).doubleValue()).sum();
	}

	@Override
	public Double calcularComissaoEstimada(Collection<Item> item) {
		return item.stream().mapToDouble(i->i.getComissaoEstimada().doubleValue()).sum();
	}

	@Override
	public Double calcularLucroBrutoEstimado(Collection<Item> item) {

		return item.stream().mapToDouble(i->i.getLucroEstimado().doubleValue()).sum();
	}

	@Override
	public Double calcularLucroLiquidoEstimado(Collection<Item> item) {
		 return item.stream().mapToDouble(i ->  i.getLucroEstimado().subtract(i.getComissaoEstimada()).doubleValue() ).sum();
	}

	@Override
	public Double calcularLucroLiquidoEstimado(Collection<Item> item, Double taxaComissaoA) {
		BigDecimal taxaComissao = BigDecimal.valueOf(Optional.of(taxaComissaoA).orElse(0d));
		
		return item.stream().mapToDouble(i ->  i.getLucroEstimado().subtract(i.getPrecoVenda().multiply(taxaComissao)).doubleValue() ).sum();
	}

	@Override
	public Double calcularCustoTotal(Collection<Item> item) {
		return item.stream().mapToDouble(i-> i.getCustoTotal().doubleValue()).sum();
	}

	@Override
	public Double calcularPrecoVendaTotal(Collection<Item> item) {
		return item.stream().mapToDouble(i -> i. getPrecoVenda().doubleValue()).sum();
	}
	
	@Transactional
	@Override
	public Collection<Item> saveAll(Collection<Item> itens){
		return itens.stream().map( i -> this.save(i)).collect(Collectors.toList());
	}
	
	





}
