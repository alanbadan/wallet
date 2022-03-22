package com.wallet.walletapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // ele busca no arquivo xml 
public class WalletapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletapiApplication.class, args);
	}

}
