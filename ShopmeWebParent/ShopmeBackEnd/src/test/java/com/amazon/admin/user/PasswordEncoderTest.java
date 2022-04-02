package com.amazon.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "jan2020";
		String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
		System.out.println(encodedPassword);

		Boolean matches = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
		System.out.println(matches);
		assertThat(matches).isTrue();
	}

}
