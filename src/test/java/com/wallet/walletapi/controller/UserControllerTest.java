package com.wallet.walletapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.runner.RunWith;
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
	
	private static final String EMAIL = "email@teste.com";
	private static final String NAME = "User Test";
	private static final String PASSWORD = "123456";
	private static final String URL = "/user";
	
	
	@MockBean // mocando o service
	UserService userService;
	
	@Autowired
	MockMvc mvc;

	public void TestSave() throws Exception {
		                                                         //aqui é o body do getJsonPayload
		mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload())
		           .contentType(MediaType.APPLICATION_JSON)
		           .accept(MediaType.APPLICATION_JSON))
		           .andExpect(status().isCreated()); // esperando o rwetorn 201 criado
		
	}
	
	public User getMockUser() {
		User user = new User();
		user.setEmail(EMAIL);
		user.setName(NAME);
		user.setPassword(PASSWORD);
		
		return user;
	}
	// metod para o payLoad load json
	public String getJsonPayload() throws JsonProcessingException { // String pa vc vai retornar um srting de json
		UserDto dto = new UserDto();
        dto.setEmail(EMAIL);
		dto.setName(NAME);
		dto.setPassword(PASSWORD);	
		
		// covretendo dto em srting 
		ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(dto);
	}
	
}
