package com.wallet.walletapi.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wallet.walletapi.security.utils.JwtTokenUtil;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private static final String AUTH_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer";
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	
	@Override // sod escrvcendo o metodo
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)throws ServletException, IOException {
		String token = request.getHeader(AUTH_HEADER);//verificando o cabeçalho da requisicao
		if(token != null && token.startsWith(BEARER_PREFIX)) {// verificando se é nilo e se tem o prefixo bearer
			token.substring(7); // ignorando o prefixo e pegando somente o token em si
		}
		String username = jwtTokenUtil.getUserNameFromToken(token);// pegando o usuario vinculado a esse token
		                          // fazendo essa coomparacao pq ainda não se ten autenticacao
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
			
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username); // buscando o usuario pelo email
			
			if(jwtTokenUtil.validToken(token)) { // verificando a vlidade do token
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
	      chain.doFilter(request, response);	// proseguindo a requizicao
	}

}
