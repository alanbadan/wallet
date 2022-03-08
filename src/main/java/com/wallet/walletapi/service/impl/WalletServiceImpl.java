package com.wallet.walletapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.wallet.walletapi.entity.Wallet;
import com.wallet.walletapi.repository.WalletRepository;
import com.wallet.walletapi.service.WalletService;

public class WalletServiceImpl implements WalletService {

	@Autowired
	WalletRepository walletRepository;
	
	@Override
	public Wallet save(Wallet wallet) {
		return walletRepository.save(wallet);
	}

}
