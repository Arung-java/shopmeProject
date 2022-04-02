package com.amazon.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.amazon.common.entity.Currency;
import com.amazon.common.entity.setting.Setting;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {
	@Autowired
	private CurrenyRepository repo;

	@Test
	public void testCreateCurrencies() {

		List<Currency> listCurrencies = Arrays.asList(new Currency("United States Dollar", "$", "USD"),
				new Currency("British Pound", "£", "GBP"), new Currency("Japanese Yen", "¥", "JPY"),
				new Currency("Euro", "€", "EUR"), new Currency("Russia Ruble", "₽", "RUB"),
				new Currency("South Korean Won", "₩", "KRW"), new Currency("South Africa Rand", "R", "ZAR"),
				new Currency("Sri Lanka Rupee", "₨", "LKR"), new Currency("Switzerland Franc", "CHF", "CHF"),
				new Currency("Zimbabwe Dollar", "Z$", "ZWD"), new Currency("India Rupee", "₹", "INR"));

		repo.saveAll(listCurrencies);
		Iterable<Currency> iterable = repo.findAll();

		assertThat(iterable).size().isGreaterThanOrEqualTo(11);

	}
	@Test
	public void testListAllOrderByNameAsc() {
		List<Currency> currencies = repo.findAllByOrderByNameAsc();
		currencies.forEach(System.out :: println);
		
		assertThat(currencies.size()).isGreaterThan(0);
	}

}
