package com.ebm;

import java.util.Random;

import org.springframework.data.domain.ExampleMatcher;

import com.ebm.estoque.domain.TipoItem;
import com.ebm.pessoal.domain.Email;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.Telefone;

public class Utils {
		
	private static Random gerador = new Random(System.currentTimeMillis());
	
	public static Telefone getRandomTelefone(boolean principal) {
		return new Telefone(null, String.valueOf(gerador.nextInt(89)+10), String.valueOf(900000000 + gerador.nextInt(99999999)), "GeradoAutomaticamente", principal);
	}
	
	
	public static ExampleMatcher getExampleMatcherForDinamicFilter(Boolean ignoreCase) {
		
		if(ignoreCase)
			return ExampleMatcher.matchingAll().withIgnoreCase()
					.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		
		return ExampleMatcher.matchingAll().
				withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
	}


	public static String getRandomCodInterno(TipoItem tipo, String nome) {
		return tipo.getDescricao()+"-"+nome+nome.hashCode();
	}


	public static Email getRandomEmail(Pessoa pf1, boolean principal) {
		// TODO Auto-generated method stub
		return new Email(null,pf1.getNome() + 100+ gerador.nextInt(899), "geradoAutomatico", principal);
	}



}
