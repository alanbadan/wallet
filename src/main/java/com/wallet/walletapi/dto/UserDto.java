package com.wallet.walletapi.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserDto {
	
	private Long id;
	@Email(message = "Email invalido")
	private String email;
	@Length(min = 3, max = 50, message = " o nome deve conter no minimo 3 caracter e no maximo 50 caracter")
	private String name;
	@NotNull
	@Length(min = 6, message = "A senha deve conter no minimo 6 caracter")
	private String password; 
	
	

}