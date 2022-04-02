package com.amazon.admin.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.amazon.admin.user.UserRepository;
import com.amazon.common.entity.User;

public class ShopmeUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repository.getUserByEmail(email);
		if (user != null) {
			return new ShopmeUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email : " + email);

	}

}
