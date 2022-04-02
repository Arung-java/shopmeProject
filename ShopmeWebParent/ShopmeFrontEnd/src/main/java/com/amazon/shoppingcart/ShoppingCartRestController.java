package com.amazon.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.Utility;
import com.amazon.common.entity.Customer;
import com.amazon.common.exception.CustomerNotFoundException;
import com.amazon.customer.CustomerService;

@RestController
public class ShoppingCartRestController {

	@Autowired
	private ShoppingCartService cartService;
	@Autowired
	private CustomerService customerService;

	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		try {
			Customer authenticatedCustomer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, authenticatedCustomer);
			return updatedQuantity + " item(s) of this product were added to your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to add this product to cart.";
		} catch (ShoppingCartException ex) {
			return ex.getMessage();
		}

	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);

	}

	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		try {
			Customer authenticatedCustomer = getAuthenticatedCustomer(request);
			float subtotal = cartService.updateQuantity(productId, quantity, authenticatedCustomer);
			System.out.println("subtotal : " + subtotal);
			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException e) {
			return "You must login to change quantity of product.";
		}
	}

	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request) {
		try {
			Customer authenticatedCustomer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, authenticatedCustomer);
			return "The product has been removed from your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}

	}

}
