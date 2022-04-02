package com.amazon.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country extends IdBasedEntity{
	
	
	@Column(nullable = false, length = 45)
	private String name;
	@Column(nullable = false, length = 5)
	private String code;
	@OneToMany(mappedBy = "country")
	private Set<State> states;

	public Country() {		
	}
	

	
	public Country(Integer id) {
		super();
		this.id = id;
	}



	public Country(String name) {
		super();
		this.name = name;
	}


	public Country(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}

	public Country(Integer id, String name, String code) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
	}


	

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}	
	
	

	



	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
	}

}
