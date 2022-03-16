package com.wallet.walletapi.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component // nâo esquecer as anotacões
public class JwtAuthenticationEntryPoint  implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, //personalisando o metodo para retornanr a msg que queros e nao a padrão
		    		"Acesso negado.Você deve estra autenticado no sistema para acessar o URL solicitada");
		
	}
	

}
