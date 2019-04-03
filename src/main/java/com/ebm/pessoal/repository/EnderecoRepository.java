package com.ebm.pessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebm.pessoal.domain.Endereco;
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

}
