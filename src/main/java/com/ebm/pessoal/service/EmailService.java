package com.ebm.pessoal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebm.exceptions.DataIntegrityException;
import com.ebm.exceptions.ObjectNotFoundException;
import com.ebm.pessoal.domain.Email;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.repository.EmailRepository;

@Service
public class EmailService {
	@Autowired
	private EmailRepository emailRepository;
	
	//insert --------------------------------------------------------------------------------------------------------
	
	public Email save(Email email) {
		
		try {
			Email result = findByEmail(email.getEmail());
			if(result.getId() != email.getId())
				throw new DataIntegrityException("O email: "+ email.getEmail() + " ja existe.");		
		}catch(ObjectNotFoundException e) {}
		
		return emailRepository.save(email);
	}


	public List<Email> saveAll(List<Email> email) {
		return email.stream().map( e -> this.save(e)).collect(Collectors.toList());
	}



	
	//update --------------------------------------------------------------------------------------------------------

	//delete --------------------------------------------------------------------------------------------------------
	public void deleteById(Integer id) {
		find(id);
		emailRepository.deleteById(id);
	}
	public void deleteAll(List<Email> email) {
		email.forEach( e -> deleteById(e.getId()));
	}
	public void deleteByPessoaId(Integer id) {
		deleteAll(findByPessoaId(id));
	}
	
	
	//find --------------------------------------------------------------------------------------------------------
	public Email find(Integer id) {
		return emailRepository.findById(id).orElseThrow( 
				() -> new ObjectNotFoundException("Não foi possivel encontrar o email de id: " + id));
	}
	public List<Email> findBy(Pessoa pessoa) {
		return findByPessoaId(pessoa.getId());
		
	}
	public List<Email> findByPessoaId(Integer id) {
		List<Email> emails = emailRepository.findByPessoa(id);
		
		if(emails.size() == 0 || emails == null) 
			throw new ObjectNotFoundException("A pessoa de id: " + id  + " não possui email cadastrado");

		return emails;
	}
	
	public Email findByEmail(String email) {
		
		return emailRepository.findByEmail(email).orElseThrow(
				() -> new ObjectNotFoundException("Não foi possivel encontrar o email."));
	}
	public List<String> getTipoEmail(){
		return emailRepository.findAllTipoEmail();
	}

}


