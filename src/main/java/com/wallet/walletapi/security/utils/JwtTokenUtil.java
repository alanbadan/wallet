package com.wallet.walletapi.security.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.sql.ordering.antlr.GeneratedOrderByFragmentRendererTokenTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIN_KEY_ROLE = "role";
	static final String CLAIN_KEY_AUDIENCE = "audience";
	static final String CLAIN_KEY_CREATED = "created";
	
	@Value("${jwt.secret}")// definido no propertities
	private String secret;
	
	@Value("{jwt.expiration}")// definido no propertities
	private Long expiration;
	
	
	public String getUserNameFromToken(String token) {
	       String username;
	       try {
	    	   Claims claims = getClaimsFromToken(token);// pegando as infomacoes
	    	   username = claims.getSubject();
	       } catch (Exception e) {
	    	   username = null;
	       }
	       return username;// no nosso caso sra o email do usuraio que sta fazendo a requisicao
	}
	
	public Date getExpirationdateFromToken(String token) {
		Date expiration;
		try {
			Claims claims = getClaimsFromToken(token);// pegando as infomacoes
			expiration = claims.getExpiration();
		}catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}
	
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {                         //pagando a chave no secret e fazemos um parse nas infomacoes e retornamos
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		}catch (Exception e) {
			claims = null;
		}
		return claims;
	}
	// metodo para verifiacr  a vidade do token 9 usando outro metodo experidetoken
	public boolean validToken(String token) {
		return !expiredToken(token);
	}
	
	public String getToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		claims.put(CLAIN_KEY_CREATED, new Date());
		userDetails.getAuthorities().forEach(authority -> claims.put(CLAIN_KEY_ROLE, authority.getAuthority()));
		
		return generateToken(claims);
	}
	
	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}
	
	private boolean expiredToken(String token) {
		Date expirationDate = this.getExpirationdateFromToken(token);
		if(expirationDate == null) {
			return false;
		}
		return expirationDate.before(new Date());
	}
	
	private String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

}
