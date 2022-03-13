package com.wallet.walletapi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.wallet.walletapi.entity.Wallet;
import com.wallet.walletapi.entity.WalletItem;
import com.wallet.walletapi.enums.TypeEnum;
import com.wallet.walletapi.repository.WalletItemRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WalletItemServiceTest {
	
	@MockBean
	WalletItemRepository walletItemRepository;
	
	@Autowired
	WalletItemService walletItemService;
	
	private static final Date DATE = new Date();
	private static final TypeEnum TYPE = TypeEnum.EN;
	private static final String DESCRIPTION = "Conta de Luz";
	private static final BigDecimal VALUE = BigDecimal.valueOf(65);	

	
	@Test
	public void testSave() {
		BDDMockito.given(walletItemRepository.save(Mockito.any(WalletItem.class))).willReturn(getMockWalletItem());
		
		WalletItem response = walletItemService.save(new WalletItem());// resgantando wallet item( o new é substituido elo mock
		
		assertNotNull(response);
		assertEquals(response.getDescripition(), DESCRIPTION);
		assertEquals(response.getValue(), VALUE);
		
	}

	private WalletItem getMockWalletItem() { //metodo para montar um objeto com as constantes declaradas
		Wallet w = new Wallet();
		w.setId(1L);
		
		WalletItem wi = new WalletItem(1L, w, DATE, TYPE, DESCRIPTION, VALUE);
		
		return wi;
	}
	@Test
	public void testFindBetweenDates() {
		List<WalletItem> list = new ArrayList<>();// començando com um alista vazio
		list.add(getMockWalletItem()); // adicioando o mock na lista
		
		Page<WalletItem> page = new PageImpl(list); // criando um page pq no repositorio se espera um page como parametro
		 //mockado no metodo com os parametros das constantes
		BDDMockito.given(walletItemRepository.findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class), Mockito.any(PageRequest.class)));
		//passando os parametros para serem substituindos pelo mock
		Page<WalletItem> response = walletItemService.findBetweenDates(1L,new Date(), new Date() , 0);
		
		
		assertNotNull(response);
		assertEquals(response.getContent().size(), 1);// verificando se ha pelo menos 1 item
		assertEquals(response.getContent().get(0).getDescripition(), DESCRIPTION);
		
	}
	@Test
	public void testFindByType() {
		List<WalletItem> list = new ArrayList<>();
		list.add(getMockWalletItem());
		//mocakando o repositorio
		BDDMockito.given(walletItemRepository.findWalletIdAndType(Mockito.anyLong(), Mockito.any(TypeEnum.class))).willReturn(list);
		
		List<WalletItem> response = walletItemService.findByWalletAndType(1L, TypeEnum.EN);
		
		
		assertNotNull(response);
		assertEquals(response.get(0).getType(), TYPE);//garantindo o retorno e o type
		
		}
	@Test
	public void testSumWallet() {
		BigDecimal value = BigDecimal.valueOf(45);
		
		BDDMockito.given(walletItemRepository.sumByWalletId(Mockito.anyLong())).willReturn(value);
		
		BigDecimal response = walletItemService.sumWalletId(1L);
		
		assertEquals(response.compareTo(value), 0);
	}
	
}

   