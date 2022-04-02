package com.amazon.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "states")
public class State extends IdBasedEntity{
	
	@Column(nullable = false, length = 45)
	private String name;
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	public State() {
		super();
		// TODO Auto-generated constructor stub
	}

	public State(String name, Country country) {
		super();
		this.name = name;
		this.country = country;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + ", country=" + country + "]";
	}

}
