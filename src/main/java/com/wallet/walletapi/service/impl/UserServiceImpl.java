package com.wallet.walletapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.walletapi.entity.User;
import com.wallet.walletapi.repository.UserRepository;
import com.wallet.walletapi.service.UserService;

@Service
public class UserServiceImpl implements UserService  {
	
	@Autowired
	UserRepository userRepository;
	
	
	public User save(User user) {
		
		return userRepository.save(user);
	}

	public Optional<User> findByEmail(String email) {
		
		return userRepository.findByEmailEquals(email);
	}

}
