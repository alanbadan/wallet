package com.wallet.walletapi.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserWalletDto {
	// passando somnete o id pq o relacionamneto entre eles é somente pelo id , e o fetcyLasy permite vc trazer oque vc quer consultar
	
	private Long id;
	@NotNull(message = "Informe o Id do usuario")
	private Long users;
	@NotNull(message = "Informe o Id da carteira")
	private Long wallet;

}
