package com.amazon.common.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.amazon.common.constants.Constants;
import com.amazon.common.entity.IdBasedEntity;

@Entity
@Table(name = "product_images")
public class ProductImage extends IdBasedEntity{
	
	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product; // here product have one or more extra images
// and this product is attribute is useing product class mappedby.

	
	

	public ProductImage(Integer id, String name, Product product) {
	super();
	this.id = id;
	this.name = name;
	this.product = product;
}

	public ProductImage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductImage(String name, Product product) {
		super();
		this.name = name;
		this.product = product;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Transient
	public String getImagePath() {
		return Constants.S3_BASE_URI+ "/product-images/" + product.getId() + "/extras/" + this.name;

	}
}
