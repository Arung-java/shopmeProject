package com.amazon.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.amazon.Utility;
import com.amazon.address.AddressService;
import com.amazon.common.entity.Address;
import com.amazon.common.entity.CartItem;
import com.amazon.common.entity.Customer;
import com.amazon.common.entity.ShippingRate;
import com.amazon.customer.CustomerService;
import com.amazon.shipping.ShippingRateService;

@Controller
public class ShoppingCartController {
	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	@Autowired private AddressService addressService;
	@Autowired private ShippingRateService rateService;
	
	@GetMapping("/cart")
	public String viewCart(Model model,HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItems = cartService.lisCartItems(customer);
		float estimatedTotal=0.0F;
		for (CartItem item : cartItems) {
			estimatedTotal += item.getSubtotal();
		}
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate=null;
		boolean usePrimaryAddressAsDefault=false;
		if(defaultAddress !=null) {
			shippingRate=rateService.getShippingRateForAddress(defaultAddress);
		}else {
			usePrimaryAddressAsDefault=true;
			shippingRate=rateService.getShippingRateForCustomer(customer);
		}
		
		model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
		model.addAttribute("shippingSupported",shippingRate !=null);
		model.addAttribute("cartItems",cartItems);
		model.addAttribute("estimatedTotal",estimatedTotal);
		return "cart/shopping_cart";
	}
	private Customer getAuthenticatedCustomer(HttpServletRequest request){
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		return customerService.getCustomerByEmail(email);
		
	}

}
