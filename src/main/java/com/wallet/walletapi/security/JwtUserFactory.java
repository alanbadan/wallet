package com.wallet.walletapi.security;

import com.wallet.walletapi.entity.User;

public class JwtUserFactory {
	
	public static JwtUser create(User user) {
		return new JwtUser(user.getId(), user.getEmail(), user.getPassword());
	}     // criando o user e passando para o construtor do JwtUser

}
 