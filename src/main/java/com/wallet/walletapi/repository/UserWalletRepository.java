package com.wallet.walletapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.walletapi.entity.UserWallet;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long>{

}
