package com.academy.onlineAcademy.exceptions;

public class NewUserException extends Exception {
	
	public enum NewUserErrorType {
		FAILED_VALIDATION, NO_USER_TYPE, EXISTING_USERNAME, EXISTING_EMAIL, PASSWORDS_NOT_MATCHING, DATABASE_FAIL
	}
	
	private NewUserErrorType newUserErrorType;
	
	public NewUserErrorType getNewUserErrorType() {
		return newUserErrorType;
	}
	
	public NewUserException(NewUserErrorType newUserErrorType) {
		this.newUserErrorType = newUserErrorType;
	}

}
