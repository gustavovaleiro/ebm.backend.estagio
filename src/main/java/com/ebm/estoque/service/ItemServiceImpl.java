package com.ebm.estoque.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebm.estoque.domain.Item;
import com.ebm.estoque.repository.ItemRepository;
import com.ebm.estoque.service.interfaces.CategoriaItemService;
import com.ebm.estoque.service.interfaces.ItemService;
import com.ebm.estoque.service.interfaces.UnidadeService;
import com.ebm.exceptions.DataIntegrityException;

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
	public List<Item> saveAll(List<Item> itens){
		return itens.stream().map( i -> this.save(i)).collect(Collectors.toList());
	}


}
