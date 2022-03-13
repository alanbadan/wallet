package com.wallet.walletapi.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.spel.ast.OpAnd;
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
	private Long savedWalletItemId = null;
	private Long savedWalletId = null;
		
	
	@Autowired
	WalletItemRepository walletItemRepository;
	
	@Autowired
	WalletRepository walletRepository;
	
	@Before
	public void SetUp() {
		Wallet w = new Wallet();
		w.setName("Carterira Teste");
		w.setValue(Long.valueOf(250));
		walletRepository.save(w); //criando e salvando uma carteira
		
		WalletItem wi = new WalletItem(null, w, DATE, TYPE, DESCRIPTION, VALUE);
		walletRepository.save(w); //cirando uma waaletItem 
		 
		savedWalletItemId = wi.getId(); // setenado o id da carteira e colocando na cariavel ( la de cima Long savedWalletItemId} 
		savedWalletId = w.getId(); // setandio o walletitem , e colocando na cariavel ( aqui a mesma coisa)
		
	}
	
	@After
	public void TearDown() {
		walletItemRepository.deleteAll();// limpa tudo walletItem
		walletRepository.deleteAll(); // limpa tudo wallet
	}
	
	
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
	@Test(expected = ConstraintViolationException.class)// metodo para tentar inserir walletItem com dados errados, não salva e retorna uma exception
	public void TestSaveInvalidWalletItem() {
		WalletItem wi = new WalletItem(null, null, DATE, null, DESCRIPTION, null);
		walletItemRepository.save(wi);
	}
	@Test
	public void testUpDate() {
		Optional<WalletItem> wi = walletItemRepository.findById(savedWalletId); // recebendo o id vindo do setUp
		
		String description = "Descricao alterada";
		
		WalletItem changed = wi.get();
		changed.setDescripition(description);
		
		walletItemRepository.save(changed); //deppis de alterar a descricao , aeui esta salvando
		
		Optional<WalletItem> newWalletItem = walletItemRepository.findById(savedWalletId); // resgatando a walletitem novamente
		
		assertEquals(description, newWalletItem.get().getDescripition());// comparando a descricao(String) esperada com a salva mo banco de dados
	}
	@Test
	public void deleteWalletItem() {
		Optional<Wallet> wallet = walletRepository.findById(savedWalletId);// busacando o id de uma waalet salvo no setUp
		WalletItem wi = new WalletItem(null, wallet.get(), DATE, TYPE, DESCRIPTION, VALUE);// cruiando uma walletItem diferente
		
		walletItemRepository.save(wi);
		walletItemRepository.deleteById(wi.getId());
		
		Optional<WalletItem> response = walletItemRepository.findById(wi.getId()); // buscando o wi id
		
		assertFalse(response.isPresent());// comparando se esa vindo vasio
	}

}
