package com.wallet.walletapi.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class WalletDto {
	
	private Long id;
	@NotNull(message = "O campo nome deve ser preenchido")
	@Length(min = 3, message = "O nome deve conter no minimo 3 carcter")
	private String name;
	@NotNull(message = "insira um valor pra a carteira")
	private Long value;

}
