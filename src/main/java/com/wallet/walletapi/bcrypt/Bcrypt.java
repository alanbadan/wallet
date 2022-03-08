package com.wallet.walletapi.bcrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Bcrypt { //criptogarafando a senha
	
	public static String gethas(String password) {
		if(password == null) {
			return null;
		}
		
		BCryptPasswordEncoder enconder = new BCryptPasswordEncoder();
		return enconder.encode(password);
	}
	
	

}
