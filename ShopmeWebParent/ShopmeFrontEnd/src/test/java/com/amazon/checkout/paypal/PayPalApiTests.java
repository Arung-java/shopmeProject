package com.amazon.checkout.paypal;



import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {

	private static final String BASE_URL="https://api.sandbox.paypal.com";
	private static final String GET_ORDER_API="/v2/checkout/orders/";
	private static final String CLIENT_ID="AZBuJy7y6ffuziprv-OeqsLOFLK3IickHCkRKvYiEdnlYrEebFbf-I-g0uTGSO4R85mr_xg87jPkzaRA";
	private static final String CLIENT_SECRET="EPB7u_MXcWWmkT5G_R3lcuLBi3sZ9q4b3QLIwO0rZiqoLziDg3Pr-j4PwE7EDILi-saFYJI3lCTVbGU7";

	
	@Test
	public void testGetOrderDetails() {
		String orderId="11P46734YT577592N";
		String requestURL= BASE_URL + GET_ORDER_API + orderId;
		
		HttpHeaders headers=new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language", "en_US");
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		
		HttpEntity<MultiValueMap<String, String>> request=new HttpEntity<>(headers);
		RestTemplate restTemplate=new RestTemplate();
		
		ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
		PayPalOrderResponse orderResponse = response.getBody();
		
		System.out.println("ORDER ID  :" + orderResponse.getId() + " Status : "+ orderResponse.getStatus());
		System.out.println(" Validated : " + orderResponse.validate(orderId));
	}
}
