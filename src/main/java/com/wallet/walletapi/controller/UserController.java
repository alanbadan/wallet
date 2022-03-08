package com.wallet.walletapi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.walletapi.dto.UserDto;
import com.wallet.walletapi.entity.User;
import com.wallet.walletapi.response.Response;
import com.wallet.walletapi.service.UserService;

@RestController
@RequestMapping("user") //rota 
public class UserController {
	
	@Autowired
	UserService userService;
	
	                
	@PostMapping                                                            //guade todoas as vaildacoes feitas no metodo no caso dto e podemos fazer noassas prorpias validacoes
	public ResponseEntity<Response<UserDto>> create (@Valid @RequestBody UserDto dto, BindingResult result){
		
		Response<UserDto> response = new Response<UserDto>();
		User user = userService.save(this.convertDtoToEntity(dto)); //precisa converter userDto em user
		response.setData(this.convertEntityToDto(user)); //convertendo para dto pq o reponse espera um dto e nao um user
		
	    return ResponseEntity.status(HttpStatus.CREATED).body(response); 
	}
	private User convertDtoToEntity(UserDto dto) {
		User user = new User();
		user.setEmail(dto.getEmail());
		user.setName(dto.getName());
		user.setPassword(dto.getPassword());
		return user;
	}
	private UserDto convertEntityToDto(User user) {
		UserDto dto = new UserDto();
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		dto.setPassword(user.getPassword());
		return dto;
	}

}
