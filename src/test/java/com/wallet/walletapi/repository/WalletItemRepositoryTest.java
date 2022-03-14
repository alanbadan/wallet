package com.wallet.walletapi.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	private static final BigDecimal VALUE = BigDecimal.valueOf(65);
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
		w.setValue(BigDecimal.valueOf(250));
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
		w.setValue(BigDecimal.valueOf(250));
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
		Optional<WalletItem> wi = walletItemRepository.findById(savedWalletItemId); // recebendo o id vindo do setUp
		
		String description = "Descricao alterada";
		
		WalletItem changed = wi.get();
		changed.setDescripition(description);
		
		walletItemRepository.save(changed); //deppis de alterar a descricao , aeui esta salvando
		
		Optional<WalletItem> newWalletItem = walletItemRepository.findById(savedWalletItemId); // resgatando a walletitem novamente
		
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
    @Test
    public void testBetweenDates() {
    	
    	Optional<Wallet> w = walletRepository.findById(savedWalletId); //regatando a waalwt inserida no Before
    	
    	LocalDateTime ldt = DATE.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();//pegando a data atual conforme declarado na constante
    	
    	Date currentDatePlusFiveDays = Date.from(ldt.plusDays(5).atZone(ZoneId.systemDefault()).toInstant());// pegando a data atual e adicionabdo 5 dias ao LocaldateTIme e atribuindo ao Date
    	Date currentDatePlusSevenDays = Date.from(ldt.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
    	
    	walletItemRepository.save(new WalletItem(null,w.get(), currentDatePlusFiveDays, TYPE, DESCRIPTION, VALUE));// criando nova WaletItem e adicionado a data + 5 dias
    	walletItemRepository.save(new WalletItem(null, w.get(), currentDatePlusSevenDays, TYPE, DESCRIPTION, VALUE));
    	
    	//Paginando
    	//Pageable pg = PageRequest.of(0, 10);
    	PageRequest pg =  PageRequest.of(0, 10); // parametro 10 é a quantidade por pagina   // pagerequeste vc nao da new e sim .of( pq é protected)                           //waalet salva, data da constatnte, data +5 dias, paginacao
    	Page<WalletItem> response = walletItemRepository.findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(savedWalletId, DATE, currentDatePlusFiveDays, pg);
    	             //response de retorno no payload  
    	assertEquals(response.getContent().size(), 2); //conteudo
    	assertEquals(response.getTotalElements(), 2); // os elementos                                                                                        ^
    	assertEquals(response.getContent().get(0).getWallet().getId(), savedWalletId);// pegando a waalet no primeiro eleneto e vendo se o mesmo salvo acima |
    	
    }
    @Test
    public void testeFindByType() {
    	List<WalletItem> response = walletItemRepository.findWalletIdAndType(savedWalletId, TYPE);// buscando a carteira e passando a cobstatnte definida
    	
    	assertEquals(response.size(), 1);// peagando o tambho da lista
    	assertEquals(response.get(0).getType(), TYPE);//  0 index , dando um get no response pra pegar o type e deve ser o mesmo passado no parametro
    	
    }
    @Test
    public void testFinByTypeSd() { // metodo para confereri se esta trazendo um tipo diferente, buscando um tipo diferente
    	
    	Optional<Wallet> w = walletRepository.findById(savedWalletId);
    	// salvando uma nova walletItem , se que com um tipo diferente
    	walletItemRepository.save(new WalletItem(null, w.get(), DATE, TypeEnum.SD, DESCRIPTION, VALUE));
    	// fazendo a co ndulta pelo tipo passado no parametro
    	List<WalletItem> response = walletItemRepository.findWalletIdAndType(savedWalletId, TypeEnum.SD);
    	
    	assertEquals(response.size(), 1);
    	assertEquals(response.get(0).getType(), TypeEnum.SD); // garatindo  que esta trazendo o tipo passado
    }
    @Test
    public void testBySumByWallet() { // metodo para somar todos itens de uma carteira
    	Optional<Wallet> w = walletRepository.findById(savedWalletId);//
    	 //criando uma walletItemno parametro esta 65 ea nova esta sendo trocada por 150.80( entao se tem dois walletItem na base)
    	walletItemRepository.save(new WalletItem(null, w.get(), DATE, TYPE, DESCRIPTION, BigDecimal.valueOf(150.80)));
    	 // metodo para somar coforme o id passadp como parametro e responsta em Long
    	BigDecimal response = walletItemRepository.sumByWalletId(savedWalletId);
    	
    	assertEquals(response.compareTo(BigDecimal.valueOf(215.8)), 0); // funcao mate. do bidDecimal , ele retorna 0 se for igual, 1 para valor maior e -1 para valor negativo
    }
    
}
	


