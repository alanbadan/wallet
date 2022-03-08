package com.wallet.walletapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.walletapi.dto.UserDto;
import com.wallet.walletapi.entity.User;
import com.wallet.walletapi.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc // anotacao para mock mvn funcionar
@ActiveProfiles("test")
public class UserControllerTest { //teste andPoint User
	
	private static final Long ID = 1L;
	private static final String EMAIL = "email@teste.com";
	private static final String NAME = "User Test";
	private static final String PASSWORD = "123456";
	private static final String URL = "/user";
	
	
	@MockBean // mocando o service
	UserService userService;
	
	@Autowired
	MockMvc mvc;
    
	@Test
	public void TestSave() throws Exception {
		
		BDDMockito.given(userService.save(Mockito.any(User.class))).willReturn(getMockUser());
		                                                         //aqui é o body do getJsonPayload
		mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload(ID, EMAIL, NAME, PASSWORD)) // passando as constantes de usuario valido
		           .contentType(MediaType.APPLICATION_JSON)
		           .accept(MediaType.APPLICATION_JSON))
		           .andExpect(status().isCreated()) // esperando o rwetorn 201 criado
		           .andExpect(jsonPath("$.data.id").value(ID))// confirmando se os dados estaso cortor entre o que esta recebendo e o esperadpo
		           .andExpect(jsonPath("$.data.email").value(EMAIL))// confirmando se os dados estaso cortor entre o que esta recebendo e o esperadpo
		           .andExpect(jsonPath("$.data.name").value(NAME))// confirmando se os dados estaso cortor entre o que esta recebendo e o esperadpo
		           .andExpect(jsonPath("$.data.password").value(PASSWORD));// confirmando se os dados estaso cortor entre o que esta recebendo e o esperadpo
	}
	//metodo para validar um usuario errado
	@Test
	public void testSaveIvalidUser() throws JsonProcessingException, Exception {
		
	mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload(ID, "email", NAME, PASSWORD)) // passando as constantes de usuario valido
	           .contentType(MediaType.APPLICATION_JSON)
	           .accept(MediaType.APPLICATION_JSON))
	           .andExpect(status().isBadRequest()) // esperando o rwetorn 201 criado
               .andExpect(jsonPath("$.data.erros[0]").value("Email invalido"));
                                 // como é uma array esta pegado o primero valor do array qie é a mensagem do dto   

	}
	
	public User getMockUser() {
		User user = new User();
		user.setId(ID);
		user.setEmail(EMAIL);
		user.setName(NAME);
		user.setPassword(PASSWORD);
		
		return user;
	}
	// metod para o payLoad load json
	public String getJsonPayload(Long id, String email, String name, String password) throws JsonProcessingException { // String pa vc vai retornar um srting de json
		UserDto dto = new UserDto();
		dto.setId(id);
        dto.setEmail(email);
		dto.setName(name);
		dto.setPassword(password);	
		
		// covretendo dto em srting 
		ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(dto);
	}
	
}
