package com.wallet.walletapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.walletapi.dto.WalletItemDto;
import com.wallet.walletapi.entity.Wallet;
import com.wallet.walletapi.entity.WalletItem;
import com.wallet.walletapi.enums.TypeEnum;
import com.wallet.walletapi.service.WalletItemService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WalletItemControllerTest {
	
	@MockBean
	WalletItemService walletItemService;
	
	@Autowired
	MockMvc mvc;
	
	private static final Long ID = 1L;
	private static final Date DATE = new Date();
	private static final LocalDate TODAY = LocalDate.now();//iniciando a cosntante com Localdate .now para pegar hr atual
	private static final TypeEnum TYPE = TypeEnum.EN;
	private static final String DESCRIPTION = "Conta de Luz";
	private static final BigDecimal VALUE = BigDecimal.valueOf(65);
	private static final String URL = "/wallet-item";
	
	@Test
	public void testSave() throws Exception { // salvando novas wallets
		
		BDDMockito.given(walletItemService.save(Mockito.any(WalletItem.class))).willReturn(getMockWalletItem());
	//***** nao esquecer do import static *********** e da Exception
		mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload())
				.contentType(MediaType.APPLICATION_JSON)// ele é aplicatioJson
				.accept(MediaType.APPLICATION_JSON)) // ele aceita aplicatioJson
		.andExpect(status().isCreated()) // esperando 201
		.andExpect(jsonPath("$.data.id").value(ID))
		.andExpect(jsonPath("$.data.date").value(TODAY.format(getDateFormater())))//converendo a data atual padronizado
		.andExpect(jsonPath("$.data.description").value(DESCRIPTION))
		.andExpect(jsonPath("$.data.type").value(TYPE.getValue()))// da um get para pegar o conteudo do enum e não o enum em si
		.andExpect(jsonPath("$.data.value").value(VALUE))
		.andExpect(jsonPath("$.data.wallet").value(ID));
	}
	
	@Test
	@WithMockUser
	public void testFindBetweenDates() throws Exception { 
		List<WalletItem> list = new ArrayList<>();//instanciando uma lista mochando as constantes
		list.add(getMockWalletItem());
		
		Page<WalletItem> page = new PageImpl<>(list); // o rerorno sendo um page passando a list para paginar
		
		String starDate = TODAY.format(getDateFormater());// comeca com adata atual
		String endDate = TODAY.plusDays(5).format(getDateFormater()); // a data atual mais 5 dias
		
		BDDMockito.given(walletItemService.findBetweenDates(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class), Mockito.anyInt())).willReturn(page);   
		                                               //o id da carteira(pathVariable)statDate(pathParams)= startDate e ensDate (variaveis criadas)
		mvc.perform(MockMvcRequestBuilders.get(URL + "/1?startDate=" + starDate + "endDate" + endDate)
				    .contentType(MediaType.APPLICATION_JSON)
				    .accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content[0].id").value(ID)) // como é uma page olha-se no primeiro lemento da pagibacao (content)
			.andExpect(jsonPath("$.data.content[0].date").value(TODAY.format(getDateFormater())))
			.andExpect(jsonPath("$.data.content[0].description").value(DESCRIPTION))
			.andExpect(jsonPath("$.data.content[0].type").value(TYPE.getValue()))
			.andExpect(jsonPath("$.data.content[0].value").value(VALUE))
			.andExpect(jsonPath("$.data.content[0].wallet").value(ID));
	}
	@Test
	@WithMockUser
	public void testFindByType() throws Exception {
		List<WalletItem> list =  new ArrayList<>();
		list.add(getMockWalletItem());
		
		BDDMockito.given(walletItemService.findByWalletAndType(Mockito.anyLong(), Mockito.any(TypeEnum.class))).willReturn(list);
		
		mvc.perform(MockMvcRequestBuilders.get(URL + "/type/1?type=ENTRADA")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.content[0].id").value(ID)) 
		.andExpect(jsonPath("$.data.content[0].date").value(TODAY.format(getDateFormater())))
		.andExpect(jsonPath("$.data.content[0].description").value(DESCRIPTION))
		.andExpect(jsonPath("$.data.content[0].type").value(TYPE.getValue()))
		.andExpect(jsonPath("$.data.content[0].value").value(VALUE))
		.andExpect(jsonPath("$.data.content[0].wallet").value(ID));
	}
	@Test
	@WithMockUser
	public void testUpdate() throws Exception{
		
		String description = "nova descricao";
		Wallet w = new Wallet();
		w.setId(ID);
		
		BDDMockito.given(walletItemService.findById(Mockito.anyLong())).willReturn(Optional.of(getMockWalletItem()));
		BDDMockito.given(walletItemService.save(Mockito.any(WalletItem.class))).willReturn(new WalletItem(1L, w, DATE, TypeEnum.SD, description, VALUE));
		
		mvc.perform(MockMvcRequestBuilders.put(URL).content(getJsonPayload())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.id").value(ID))
		.andExpect(jsonPath("$.data.date").value(TODAY.format(getDateFormater())))
		.andExpect(jsonPath("$.data.description").value(description))
		.andExpect(jsonPath("$.data.type").value(TypeEnum.SD.getValue()))
		.andExpect(jsonPath("$.data.value").value(VALUE))
		.andExpect(jsonPath("$.data.wallet").value(ID));
		
	}
	@Test
	@WithMockUser
	public void testUpdateWalletChange() throws Exception {
		
		Wallet w = new Wallet();
		w.setId(99L);
		
		WalletItem wi = new WalletItem(1L, w, DATE, TypeEnum.SD, DESCRIPTION, VALUE);
		BDDMockito.given(walletItemService.findById(Mockito.anyLong())).willReturn(Optional.of(wi));
		
		mvc.perform(MockMvcRequestBuilders.put(URL).content(getJsonPayload())
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.erros[0]").value("Voce não pode alterar a carteira"));
		            
	}
	@Test
	@WithMockUser
	public void testUpdateInvalidId() throws Exception {
		
		BDDMockito.given(walletItemService.findById(Mockito.anyLong())).willReturn(Optional.empty());
		
		mvc.perform(MockMvcRequestBuilders.put(URL).content(getJsonPayload())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.data").doesNotExist())
		.andExpect(jsonPath("$.errors[0]").value("WalletItem não encontrado"));
		
	}
	
	@Test
	@WithMockUser
//	@WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
	public void testDelete() throws JsonProcessingException, Exception {
		
		BDDMockito.given(walletItemService.findById(Mockito.anyLong())).willReturn(Optional.of(new WalletItem()));
		
		mvc.perform(MockMvcRequestBuilders.delete(URL+"/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data").value("WalletItem de id "+ ID +" apagada com sucesso"));
	}
	
	@Test
	@WithMockUser
//	@WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
	public void testDeleteInvalid() throws Exception {
		
		BDDMockito.given(walletItemService.findById(Mockito.anyLong())).willReturn(Optional.empty());
		
		mvc.perform(MockMvcRequestBuilders.delete(URL+"/99")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.data").doesNotExist())
		.andExpect(jsonPath("$.errors[0]").value("WalletItem de id "+ 99 + " não encontrada"));
	
	}
// metodo para mocar os parametros
	private WalletItem getMockWalletItem() {
		Wallet w = new Wallet();//instanciando um wallet e crio um Id.
		w.setId(1L);
		
		WalletItem wi = new WalletItem(1L, w, DATE, TYPE, DESCRIPTION, VALUE);
		return wi;
	}
     //metodo para o payload Json
	public String getJsonPayload() throws JsonProcessingException {
		WalletItemDto dto = new WalletItemDto(); // inicio uma walletdto e instanciao as constantes e converto para string
		dto.setId(ID);
		dto.setDate(DATE);
		dto.setDescription(DESCRIPTION);
		dto.setType(TYPE.getValue());
		dto.setValue(VALUE);
		dto.setWallet(ID);
		
		ObjectMapper mapper = new ObjectMapper(); // para retornar uma srting em objeto Json 
		return mapper.writeValueAsString(dto);
	
     }
	// metodo
	private DateTimeFormatter getDateFormater() { // formatendo a hora para o padrao especificado 
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyy");
		return formater;
	}

}
