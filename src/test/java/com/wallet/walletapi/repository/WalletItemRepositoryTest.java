package com.wallet.walletapi.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.wallet.walletapi.entity.Wallet;
import com.wallet.walletapi.entity.WalletItem;
import com.wallet.walletapi.enums.TypeEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")

public class WalletItemRepositoryTest {
	
	private static final Date DATE = new Date();
	private static final TypeEnum TYPE = TypeEnum.EN;
	private static final String DESCRIPTION = "CONTA DE LUZ";
	private static final Long VALUE = Long.valueOf(65);
		
	
	@Autowired
	WalletItemRepository walletItemRepository;
	
	@Autowired
	WalletRepository walletRepository;
	
	
	@Test
	public void testSave() {
		
		Wallet w = new Wallet(); //instanciando uma nova carteira
		w.setName("Carteira");
		w.setValue(Long.valueOf(500));
		walletRepository.save(w); // usando a iterface para salvaar uma nova carteira, que vai ser recebida no construtor abaixo
		
		
		WalletItem wl = new WalletItem(1L, w, DATE, TYPE, DESCRIPTION, VALUE); //passando os argumentos pelo consrutor
		WalletItem response = walletItemRepository.save(wl);
		
		assertNotNull(response);
		assertEquals(response.getDescripition(), DESCRIPTION);
		assertEquals(response.getType(), TYPE);
		assertEquals(response.getValue(), VALUE);
		assertEquals(response.getWallet().getId(), w.getId()); // comparando o id recebido do new Wallet w.
		
	}

}
