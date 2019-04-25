package com.ebm;

import java.util.Random;

import org.springframework.data.domain.PageRequest;

import com.ebm.pessoal.domain.Telefone;

public class Utils {
		
	private static Random gerador = new Random(System.currentTimeMillis());
	
	public static Telefone getRandomTelefone() {
		return new Telefone(null, String.valueOf(gerador.nextInt(89)+10), String.valueOf(900000000 + gerador.nextInt(99999999)), "GeradoAutomaticamente");
	}

}
