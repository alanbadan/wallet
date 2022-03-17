package com.wallet.walletapi.config;

import javax.tools.DocumentationTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.wallet.walletapi.security.utils.JwtTokenUtil;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Profile("dev")//sempre se deve passar o profile seja de dev ou hml
@EnableSwagger2
public class SwaggerConfig {

	@Autowired
	JwtTokenUtil jwtTokenUtil; //pq vamos preciar de token para autenticar o Swagger
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.wallet.walleapi.controller")) //nao esquecer dos pacotes dos caminhos da aplicacao
				.paths(PathSelectors.any()).build()
				.apiInfo(apiInfo()); //passando algumas informacoes sobre a documentcao
	}
	//metodo apiInfo
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Wallet api")// titulo
				.description("Wallet API - Documentacao de acesso aos endPoints.").version("1.0")//decricao e versao que se esta trablhANDO
				.build();
	}
/*	 aula 59 rever pq esse metodo passa um token para o ambiente de dev no postgre e estou somente no H2
	@Bean //metodo para o swagger bater no endpoint restritos
	public SecurityConfiguration securty() {
		String token;
		try {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername("development@swagger.user");// cirnado um usuario especifico para o swagger
			token = this.jwtTokenUtil.getToken(userDetails);// resgatamos o usuario e geramos um token para ele
		}catch (Exception e) {
			token = "";
		}
		return new SecurityConfiguration(null, null, null, null, "Bearer"+ token, ApiKeyVehicle.HEADER, "Authorization", ",");
		
	}
	*/
}
