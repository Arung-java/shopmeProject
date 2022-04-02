package com.amazon.order;

public class OrderReturnResponse {

	private Integer orderId;

	
	public OrderReturnResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderReturnResponse(Integer orderId) {
		super();
		this.orderId = orderId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	
}
