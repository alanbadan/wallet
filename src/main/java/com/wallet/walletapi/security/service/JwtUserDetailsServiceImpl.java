package com.wallet.walletapi.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.wallet.walletapi.entity.User;
import com.wallet.walletapi.security.JwtUserFactory;
import com.wallet.walletapi.service.UserService;

public class JwtUserDetailsServiceImpl  implements UserDetailsService {

	@Autowired
	UserService userService;// classe do usuario
	
	@Override // sob escrevendo o metodo           // busacando por email
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = userService.findByEmail(email);// buscando na base conforme o email passado no parametro sendo um optional pode existir ou não
		
		if(user.isPresent()) {
			return JwtUserFactory.create(user.get());// validando o usuario encontardo.
		}
		throw new UsernameNotFoundException("email não encontrado");// caso não exista retorna uma expecption
	}

}
