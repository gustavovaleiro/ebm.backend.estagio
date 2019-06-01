package com.ebm.estoque.service.interfaces;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ebm.estoque.domain.Item;
import com.ebm.estoque.dtos.ItemListDTO;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;

@Service
public interface ItemService {

	String DATAINTEGRITY_UNIDADENULL = DataIntegrityException.DEFAULT + ": Item sem unidade.";
	String DATAINTEGRITY_CATEGORIANULL = DataIntegrityException.DEFAULT + ": Item sem categoria.";
	String ONFE_BYID = ObjectNotFoundException.DEFAULT + "um item com o id passado";
	String DATAINTEGRITY_IDNULL =DataIntegrityException.DEFAULT + ": o id passado é nulo.";



	Item save(Item item);
	Collection<Item> saveAll(Collection<Item> itens);
	Item findById(Integer id);
	void deleteAll(boolean propaga);
	void delete(Integer id);
	Page<ItemListDTO> findBy(String codigoInterno, String tipo, String nome, String unidade, String Categoria, PageRequest page);
	
	List<Item> findBy(String codigoInterno, String tipo, String nome, String unidade, String Categoria);
	
	Double calcularComissaoEstimada(Collection<Item> item, Double taxaComissao);
	Double calcularComissaoEstimada(Collection<Item> item);
	Double calcularLucroBrutoEstimado(Collection<Item> item);
	Double calcularLucroLiquidoEstimado(Collection<Item> item);
	Double calcularLucroLiquidoEstimado(Collection<Item> item, Double taxaComissao);
	Double calcularCustoTotal(Collection<Item> item);
	Double calcularPrecoVendaTotal(Collection<Item> item);
}
