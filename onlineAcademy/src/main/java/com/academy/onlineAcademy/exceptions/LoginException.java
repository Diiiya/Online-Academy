package com.academy.onlineAcademy.exceptions;


public class LoginException extends Exception {
	
	public enum LoginErrorType {
		USERNAME_EXISTS, INVALID_USERNAME_PASSWORD_COMBINATION, INVALID_USER_TYPE;
	}
	
	private LoginErrorType errorType;
	
	public LoginErrorType getErrorType() {
		return errorType;
	}
	
	public LoginException(LoginErrorType errorType) {
		this.errorType = errorType;
	}
}
