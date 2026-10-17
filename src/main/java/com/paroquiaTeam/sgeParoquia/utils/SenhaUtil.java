package com.paroquiaTeam.sgeParoquia.utils;

import java.util.Iterator;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {
	private static final int CUSTO = 12;
	
	public static String hash(String senha) {
		return BCrypt.hashpw(senha, BCrypt.gensalt(CUSTO));
	}
	
	public static boolean isSenhaBate(String senhaPura, String senhaHash) {
		return BCrypt.checkpw(senhaPura, senhaHash);
	}
	
	public static boolean validarRequisitosSenha(String senha, String login) {
		// Senha não pode ser igual a login
		if (senha.equals(login)) {
			return false;
		}
		
		if (senha.length() < 10) {
			return false;
		}
		
		boolean temNumero = false;
		boolean temMaiuscula = false;
		boolean temMinuscula = false;
		for (char ch : senha.toCharArray()) {
			if (Character.isDigit(ch)) {
				temNumero = true;
			} 
			else if (Character.isUpperCase(ch)) {
				temMaiuscula = true;
			}
			else if (Character.isLowerCase(ch)) {
				temMinuscula = true;
			}
			
			if (temNumero && temMaiuscula && temMinuscula) {
				return true;
			}
		}
		
		// Se chegou aqui, falhou o teste de requerimento
		return false;
	}
}
