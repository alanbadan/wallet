package com.wallet.walletapi.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // ele nao retrona valores null na resposta do payload
public class UserDto {
	
	private Long id;
	@Email(message = "Email invalido")
	private String email;
	@Length(min = 3, max = 50, message = " o nome deve conter no minimo 3 caracter e no maximo 50 caracter")
	private String name;
	@NotNull
	@Length(min = 6, message = "A senha deve conter no minimo 6 caracter")
	private String password; 
	@NotNull(message = "Informe uma role de acesso")
	@Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)$", message = "Para o role somente sâo aceitos os valores ROLE_ADMIN ou ROLE_USER")
	private String role;//expresao regulao pra comparar a veracidade do dados recebidos
	
	

}
