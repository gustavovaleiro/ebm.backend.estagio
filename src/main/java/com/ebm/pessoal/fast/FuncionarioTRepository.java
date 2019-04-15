package com.ebm.pessoal.fast;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioTRepository extends JpaRepository<FuncionarioT, Integer>  {

}
