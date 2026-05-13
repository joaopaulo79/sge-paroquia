package com.paroquiaTeam.sgeParoquia.utils;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {
	private static final int CUSTO = 12;
	
	public static String hash(String senha) {
		return BCrypt.hashpw(senha, BCrypt.gensalt(CUSTO));
	}
	
	public static boolean verificar(String senhaPura, String senhaHash) {
		return BCrypt.checkpw(senhaPura, senhaHash);
	}
}
