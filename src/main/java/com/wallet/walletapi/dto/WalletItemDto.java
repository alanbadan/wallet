package com.wallet.walletapi.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class WalletItemDto {
	
	private Long id;
	@NotNull(message = "insira o Id da carteira")
	private Long wallet;
	@NotNull(message = "Informe uma date") //
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
	private Date date;
	@NotNull(message = "Informe um tipo")
	@Pattern(regexp = "^(ENTRADA|SAIDA)$", message = "Para o tipo somente sâo aceitos os valores ENTRADA ou SAIDA")
	private String type;//expresao regulao pra comparar a veracidade do dados recebidos
	@NotNull
	@Length(min = 3, message = "A descricao deve conter no minimo 3 caracteres")
	private String description;
	@NotNull(message = "informe um valor")
	private BigDecimal value;

}
