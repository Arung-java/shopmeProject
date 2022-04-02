package com.amazon.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currencies")
public class Currency extends IdBasedEntity{
	
	@Column(nullable = false, length = 64)
	private String name;
	@Column(nullable = false, length = 3)
	private String symbol;
	@Column(nullable = false, length = 4)
	private String code;

	public Currency(String name, String symbol, String code) {
		super();
		this.name = name;
		this.symbol = symbol;
		this.code = code;
	}

	public Currency(Integer id, String name, String symbol, String code) {
		super();
		this.id = id;
		this.name = name;
		this.symbol = symbol;
		this.code = code;
	}

	public Currency() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return name+ " - "+ code +" - "+symbol;
	}

}
