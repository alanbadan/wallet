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

import com.wallet.walletapi.dto.UserWalletDto;
import com.wallet.walletapi.entity.User;
import com.wallet.walletapi.entity.UserWallet;
import com.wallet.walletapi.entity.Wallet;
import com.wallet.walletapi.response.Response;
import com.wallet.walletapi.service.UserWalletService;

@RestController
@RequestMapping("user-wallet")
public class UserWalletController {
	
	@Autowired // acessando o service para salvar novas entidades
	UserWalletService userWalletService;
	
	
	@PostMapping
	public ResponseEntity<Response<UserWalletDto>> create(@Valid @RequestBody UserWalletDto dto, BindingResult result){
		Response<UserWalletDto> response = new Response<UserWalletDto>();
		if(result.hasErrors()) {
			result.getAllErrors().forEach(r -> response.getErros().add(r.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		UserWallet userWallet = userWalletService.save(this.convertDtoToEntity(dto));
		
		response.setData(this.convertEntityToDto(userWallet));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

	//convertendo dto para entidade
	public UserWallet convertDtoToEntity(UserWalletDto dto) {
		UserWallet userWallet = new UserWallet();
		User user = new User(); // se tentar setter o tipo user nao se aplica para o tipo long,pq ele espera um anetiarde tipo user
		user.setId(dto.getUsers()); // se inicai um entidade user, e preenchendo somente com o id que esta vindo do dto ( Lazy)
		Wallet wallet = new Wallet();
		wallet.setId(dto.getWallet());
		
		userWallet.setId(dto.getId());
		userWallet.setUser(user); // adicionando 
		userWallet.setWallet(wallet);
		
		return userWallet;
	}
	//convertendo Entity para dto
	public UserWalletDto convertEntityToDto(UserWallet userWallet) {
		UserWalletDto dto = new UserWalletDto();
		dto.setId(userWallet.getId());
		dto.setUsers(userWallet.getUser().getId());
		dto.setWallet(userWallet.getWallet().getId());
		
		return dto;
	}
	
	
}
