package com.wallet.walletapi.service;

import java.util.Optional;

import com.wallet.walletapi.entity.User;

public interface UserService {
	
	User save(User user);
	
	Optional<User> findByEmail(String email);
	
	

}
