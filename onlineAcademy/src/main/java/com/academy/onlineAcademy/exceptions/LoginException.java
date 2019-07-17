package com.academy.onlineAcademy.exceptions;


public class LoginException extends Exception {
	
	/**
	 * Enum of error types thrown when user logs in
	 * @author d.boyadzhieva
	 *
	 */
	public enum LoginErrorType {
		USERNAME_EXISTS, INVALID_USERNAME_PASSWORD_COMBINATION, INVALID_USER_TYPE;
	}
	
	private LoginErrorType errorType;
	
	/**
	 * Method that gets the type of login error
	 * @return
	 */
	public LoginErrorType getErrorType() {
		return errorType;
	}
	
	/**
	 * Class constructor
	 * @param courseErrorType
	 */
	public LoginException(LoginErrorType errorType) {
		this.errorType = errorType;
	}
}
