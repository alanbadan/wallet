package com.wallet.walletapi.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wallet.walletapi.entity.User;
import com.wallet.walletapi.security.utils.enums.RoleEnum;

//vc escreve o test
//tste falha
//codifica normalmente
//teste passa
//refatora


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
	
	// craindo uma constate para facilitar
	private static final String EMAIL = "teste@teste.com";
	
	
	@Autowired
	UserRepository userRepository;
	
	
	@Before  // metodo que realiza as intrucoes desse metodo ANTES DE tudo
	public void setUp() {
		User user = new User();
		user.setName("set up user");
		user.setPassword("Senha123");
		user.setEmail(EMAIL);
		user.setRole(RoleEnum.ROLE_ADMIN); // setando a role admin pa=q foi implemtado no jwt 
		
		userRepository.save(user);
	}
	
	
	@After
	public void teardown() {
		userRepository.deleteAll(); // apaga todos dados depois do teste
		
	}
	
	@Test //tste para buscar um usuario
	public void testSave() {
		User user = new User();
		user.setName("Teste");
		user.setPassword("123456");
		user.setEmail("teste@teste.com");
		user.setRole(RoleEnum.ROLE_ADMIN); // setando a role admin pa=q foi implemtado no jwt
		
		User response = userRepository.save(user);
		assertNotNull(response);
	}
   // metodo para buscar um usruario poe email
	public void testfindByEmail() {
		Optional<User> response = userRepository.findByEmailEquals(EMAIL);
		
		assertTrue(response.isPresent());
		assertEquals(response.get().getEmail(), EMAIL); //compararndo o email passado co o da cnstatnte
	}
}
