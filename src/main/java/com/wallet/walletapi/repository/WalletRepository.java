package com.wallet.walletapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.walletapi.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long>{

}
