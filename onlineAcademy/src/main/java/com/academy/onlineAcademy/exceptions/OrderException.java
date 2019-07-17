package com.academy.onlineAcademy.exceptions;

public class OrderException extends Exception {
	
	/**
	 * Enum of error types thrown when placing an order
	 * @author d.boyadzhieva
	 *
	 */
	public enum OrderErrorType {
		NO_ORDERS_FOUND, NOT_EXISTING_EMAIL, ORDER_FAILED
	}
	
	private OrderErrorType orderErrorType;
	
	/**
	 * Method that gets the type of error for placing an order
	 * @return
	 */
	public OrderErrorType getOrderErrorType() {
		return orderErrorType;
	}
	
	/**
	 * Class constructor
	 * @param orderErrorType
	 */
	public OrderException(OrderErrorType orderErrorType) {
		this.orderErrorType = orderErrorType;
	}

}
