package com.wallet.walletapi.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.wallet.walletapi.entity.WalletItem;
import com.wallet.walletapi.enums.TypeEnum;

public interface WalletItemService {
	
	WalletItem save(WalletItem walletItem);
	                                               // data inicial, date final , ea pagina que quer que retorna o index snpre comeca do zero
	Page<WalletItem> findBetweenDates(Long wallet, Date start, Date end, int page);
	
	List<WalletItem> findByWalletAndType(Long wallet, TypeEnum type);
	
	BigDecimal sumWalletId(Long wallet);
	
	Optional<WalletItem> findById(Long id);
	
	

}
