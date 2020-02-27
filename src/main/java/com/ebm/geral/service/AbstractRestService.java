package com.ebm.geral.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebm.geral.domain.AbstractEntity;
import com.ebm.geral.exceptions.DataIntegrityException;
import com.ebm.geral.exceptions.ObjectNotFoundException;

public abstract class  AbstractRestService < ID,Entity extends AbstractEntity<ID>> {
	public Entity save(Entity entity) {
		if(validateEntityForSave(entity)) {
			return this.getRepository().save(entity);
		}
		throw new DataIntegrityException("Erro de integridade dos dados para a entidade do tipo " + this.getGenericName());
	}
	
	public void deleteById(ID id) {
		this.findById(id);
		this.getRepository().deleteById(id);
	}
	public Entity findById(ID id) {
		return this.getRepository().findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Não foi possivel encontrar um(a) " + this.getGenericName() + " com o id"+ id));
	}
	
	public List<Entity> get(){
		return this.getRepository().findAll();
	}
	
	@SuppressWarnings("unchecked")
	private String getGenericName()
    {
        return ((Class<Entity>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]).getTypeName();
    }
	public abstract boolean validateEntityForSave(Entity entity);
	public abstract JpaRepository<Entity, ID> getRepository();
}
