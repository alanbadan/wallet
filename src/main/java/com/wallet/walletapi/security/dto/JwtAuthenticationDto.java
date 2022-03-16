package com.wallet.walletapi.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("deprecation")
@Data
public class JwtAuthenticationDto {
	
	@NotNull(message = "Informe um email")
	@NotEmpty(message = "Informe um email")
	private String email;
	@NotNull(message = "Informe um senha")
	@NotEmpty(message = "Informe um senha")
	private String password;

}
