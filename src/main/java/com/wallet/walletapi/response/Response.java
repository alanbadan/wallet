package com.wallet.walletapi.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//classe para tartar as respostas da api
@Getter
@Setter
@NoArgsConstructor
public class Response<T> { // do tipo T sera utilizada no momentpo da utilizaçao do response de acordo com o oque vc passar ele ira assumir o modelo passado para montar a classe
	
	private T data; // retorno na api(useuasrio , carteira...
	private List<String> erros; //caso de erro(validadcao...) retorna um padrao de resposta

}
