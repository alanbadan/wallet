package com.wallet.walletapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.walletapi.entity.WalletItem;

public interface WalletItemRepository extends JpaRepository<WalletItem, Long> {

}
