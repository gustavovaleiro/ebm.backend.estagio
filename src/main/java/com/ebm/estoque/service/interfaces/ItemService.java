package com.ebm.estoque.service.interfaces;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.Item;
import com.ebm.exceptions.DataIntegrityException;

@Service
public interface ItemService {

	String DATAINTEGRITY_UNIDADENULL = DataIntegrityException.DEFAULT + ": Item sem unidade.";
	String DATAINTEGRITY_CATEGORIANULL = DataIntegrityException.DEFAULT + ": Item sem categoria.";

	void deleteAll(boolean b);

	Item save(Item item);
	
	Double calcularComissaoEstimada(Collection<Item> item, Double taxaComissao);
	Double calcularComissaoEstimada(Collection<Item> item);
	Double calcularLucroBrutoEstimado(Collection<Item> item);
	Double calcularLucroLiquidoEstimado(Collection<Item> item);
	Double calcularLucroLiquidoEstimado(Collection<Item> item, Double taxaComissao);
	Double calcularCustoTotal(Collection<Item> item);
	Double calcularPrecoVendaTotal(Collection<Item> item);

	Collection<Item> saveAll(List<Item> itens);
}
