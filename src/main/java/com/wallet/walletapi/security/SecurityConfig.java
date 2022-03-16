package com.wallet.walletapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Quando vc coloca no pom o VAliDATION ele bloquei o acesso a api , ele exige o token essa classe é para desebiliatr momentaneamente
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //anotacao para a nova implementacao
public class SecurityConfig extends WebSecurityConfigurerAdapter {
/*	metodo de liberacao
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable().exceptionHandling().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeHttpRequests()
		.antMatchers("**")
		.permitAll().anyRequest().authenticated();
	}
*/
	@Autowired
	JwtAuthenticationEntryPoint unauthorizedHandler;// injecao para retorna da msg personalisada
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder()); //passando o userDateils injetado	
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return  new BCryptPasswordEncoder();
	}
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean // metodo para filtro de requisições usando a classe Jwt...Filter
	public JwtAuthenticationTokenFilter authenticationTokenFilter () throws Exception {
		return new JwtAuthenticationTokenFilter();
	}
     	 
	@Bean // metod para configuracao das rotas
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeHttpRequests()
		.antMatchers("/auth/**", "/configuration/security", "/webjars/**", "/user/**") // deixando acessivel9sem token) somentes essas rotas e o jars do projeto 
		.permitAll().anyRequest().authenticated();
		http.addFilterBefore(authenticationTokenFilterBean, UsernamePasswordAuthenticationFilter.class);
		http.headers().cacheControl(); // controle de cache no cabecalho
		
	}
}
