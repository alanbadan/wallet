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

import com.wallet.walletapi.dto.WalletDto;
import com.wallet.walletapi.entity.Wallet;
import com.wallet.walletapi.response.Response;
import com.wallet.walletapi.service.WalletService;

@RestController
@RequestMapping("wallet")
public class WalletController {
	
	@Autowired
	WalletService walletService;
	
	@PostMapping
	public ResponseEntity<Response<WalletDto>> create(@Valid @RequestBody WalletDto dto, BindingResult result){
		
		Response<WalletDto> response = new Response<WalletDto>();
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(r -> response.getErros().add(r.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Wallet wallet = walletService.save(this.convertDtoToEntity(dto));
		 response.setData(this.convertEntityToDto(wallet));
		 return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
  
	private Wallet convertDtoToEntity(WalletDto dto) {
		Wallet wallet = new Wallet();
		wallet.setId(dto.getId());
		wallet.setName(dto.getName());
		wallet.setValue(dto.getValue());
		return wallet;
	}
	
	private WalletDto convertEntityToDto(Wallet wallet) {
		WalletDto dto = new WalletDto();
		dto.setId(wallet.getId());
		dto.setName(wallet.getName());
		dto.setValue(wallet.getValue());
		return dto;
	}
}
