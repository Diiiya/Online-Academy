package com.academy.onlineAcademy.exceptions;

public class OrderException extends Exception {
	
	public enum OrderErrorType {
		NO_ORDERS_FOUND, NOT_EXISTING_EMAIL, ORDER_FAILED
	}
	
	private OrderErrorType orderErrorType;
	
	public OrderErrorType getOrderErrorType() {
		return orderErrorType;
	}
	
	public OrderException(OrderErrorType orderErrorType) {
		this.orderErrorType = orderErrorType;
	}

}
