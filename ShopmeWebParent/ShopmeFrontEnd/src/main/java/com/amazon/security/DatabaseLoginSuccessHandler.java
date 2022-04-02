package com.amazon.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.amazon.common.entity.AuthenticationType;
import com.amazon.common.entity.Customer;
import com.amazon.customer.CustomerService;
@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
@Autowired
	private CustomerService customerService;

@Override
public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws ServletException, IOException {
	CustomerUserDatails userDetails = (CustomerUserDatails) authentication.getPrincipal();
	 Customer customer = userDetails.getCustomer();
	customerService.updateAuthentiationType(customer,AuthenticationType.DATABASE);
	super.onAuthenticationSuccess(request, response, authentication);
}


}
