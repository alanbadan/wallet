package com.wallet.walletapi.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.wallet.walletapi.entity.WalletItem;
import com.wallet.walletapi.enums.TypeEnum;
import com.wallet.walletapi.repository.WalletItemRepository;
import com.wallet.walletapi.service.WalletItemService;



@Service
public class WalletItemServiceImpl implements WalletItemService {
	
	@Autowired
	WalletItemRepository walletItemRepository;
	
	@Value("${pagination.items_per_page}")// com a anotacao @value vc resgata a variavel declarada no properties
	private int itemsPerPage; //setendo em uma variavel o value

	@Override
	@CacheEvict(value = "findByWalletIdAndType", allEntries = true ) // ele apaga toda entrada de chache na entrada( apagando tudo cache passado no metodo) e atualizando para evitar de pegar dados atualizados
	public WalletItem save(WalletItem walletItem) {
		return walletItemRepository.save(walletItem);
	}

	@Override
	public Page<WalletItem> findBetweenDates(Long wallet, Date start, Date end, int page) {
		
		@SuppressWarnings("deprecation")  // a pagina que quero buscar , e a quant. de item por pagina
		PageRequest pg = PageRequest.of(page, itemsPerPage);
		
		return  walletItemRepository.findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(wallet, start, end, pg);
	}
	@Cacheable(value =  "findByWalletAndType")// passando o metodo que vai usar o cache la no arquivo xml , pode colocar vairos metoods 
	@Override
	public List<WalletItem> findByWalletAndType(Long wallet, TypeEnum type) {
		return walletItemRepository.findByWalletIdAndType(wallet, type);
	}
	@Override
	public BigDecimal sumWalletId(Long wallet) {
		return walletItemRepository.sumByWalletId(wallet);
	}

	@Override
	public Optional<WalletItem> findById(Long id) {
	     return walletItemRepository.findById(id);
	}

	@Override
	@CacheEvict(value = "findByWalletIdAndType", allEntries = true )// ele apaga toda entrada de chache na entrada( apagando tudo cache passado no metodo) e atualizando para evitar de pegar dados atualizados
	public void deleteById(Long id) {
		walletItemRepository.deleteById(id);
		
	}
	
	
	

}
