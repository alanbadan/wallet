package com.wallet.walletapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.wallet.walletapi.entity.UserWallet;
import com.wallet.walletapi.repository.UserWalletRepository;
import com.wallet.walletapi.service.UserWalletService;

public class UserWalletServiceImpl implements UserWalletService{

	@Autowired
	UserWalletRepository userWalletRepository;
	
	
	@Override
	public UserWallet save(UserWallet userWallet) {
	     return userWalletRepository.save(userWallet);
	}
}

