package com.ebm.geral.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;import java.util.Optional;
import java.util.Random;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ebm.estoque.domain.enums.TipoItem;
import com.ebm.pessoal.domain.Email;
import com.ebm.pessoal.domain.HistoricoCadastral;
import com.ebm.pessoal.domain.Pessoa;
import com.ebm.pessoal.domain.Telefone;
import com.ebm.security.Usuario;

public class Utils {
		
	private static Random gerador = new Random(System.currentTimeMillis());
	private static DateTimeFormatter brFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
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
		String replace = pf1.getNome();
		return new Email(null,replace.replace(" ", ".") + 100+ gerador.nextInt(899) +"@example.com", "geradoAutomatico", principal);
	}


	public static DateTimeFormatter getBrDateTimeFormatter() {
		// TODO Auto-generated method stub
		return brFormat ;
	}
	
	public static void audita(HistoricoCadastral historico) {
		Usuario principal = null;
		if(Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).isPresent())
			principal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(historico.getUsuarioCadastro() == null ) {
			historico.setDataCadastro(LocalDateTime.now());
			historico.setUsuarioCadastro(principal);
		}else {
			historico.setDataUltimaModificacao(LocalDateTime.now());
			historico.setUltimaModificacao(principal);
		}
	}


}
