package com.amazon.common.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role extends IdBasedEntity {
	
	@Column(length = 40, nullable = false, unique = true)
	private String name;
	@Column(length = 150, nullable = false)
	private String description;

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(id, other.id);
	}



	public Role(Integer id) {
		super();
		this.id = id;
	}



	public Role(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}


	public Role(Integer id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
