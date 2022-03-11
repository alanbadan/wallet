package com.wallet.walletapi.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class WalletItemDto {
	
	private Long id;
	@NotNull(message = "insira o Id da carteira")
	private Long wallet;
	@NotNull(message = "Informe uma date")
	private Date date;
	@NotNull(message = "Informe um tipo")
	private String type;
	@NotNull
	@Length(min = 3, message = "A descricao deve conter no minimo 3 caracteres")
	private String description;
	@NotNull(message = "informe um valor")
	private Long value;

}
