package com.wallet.walletapi.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wallet.walletapi.entity.WalletItem;
import com.wallet.walletapi.enums.TypeEnum;

public interface WalletItemRepository extends JpaRepository<WalletItem, Long> {
                     // metodo JpA filtre todos por id   e datas maiores ou iguasi as passadas no parametro Date init e menos igaual a data                                                           
	Page<WalletItem> findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(Long wallet, Date init, Date end, Pageable page);
	// retornando uma lista pelo JPA ( em ambos os cadso o ID é chave ESTRANGEIRA)
	List<WalletItem> findWalletIdAndType(Long wallet, TypeEnum type);
	
	//
	@Query(value = "SELECT SUM(value) FROM WALLETITEM wi WHERE wi.wallet.id = :wallet")
	BigDecimal sumByWalletId(@Param("wallet") Long wallet);
	
}
