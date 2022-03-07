package com.wallet.walletapi.service;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.wallet.walletapi.entity.User;
import com.wallet.walletapi.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
	
	@MockBean
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@Before
	public void setup() {
		BDDMockito.given(userRepository.findByEmailEquals(Mockito.anyString())).willReturn(Optional.of(new User()));
	}
    @Test
    public void testFindByEmail() {
    	Optional<User> user = userService.findByEmail("eamil@teste.com");
    	
    	assertTrue(user.isPresent());
    }
	
}
