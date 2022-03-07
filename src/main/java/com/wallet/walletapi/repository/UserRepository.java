package com.wallet.walletapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.walletapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	
	Optional<User> findByEmailEquals(String email);
}
