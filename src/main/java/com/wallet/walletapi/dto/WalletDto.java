package com.wallet.walletapi.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class WalletDto {
	
	private Long Id;
	@NotNull
	@Length(min = 3)
	private String name;
	@NotNull
	private Long value;

}
