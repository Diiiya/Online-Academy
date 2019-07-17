package com.academy.onlineAcademy.exceptions;

public class NewUserException extends Exception {
	
	/**
	 * Enum of error types thrown when a new user is created
	 * @author d.boyadzhieva
	 *
	 */
	public enum NewUserErrorType {
		FAILED_VALIDATION, NO_USER_TYPE, EXISTING_USERNAME, EXISTING_EMAIL, PASSWORDS_NOT_MATCHING, DATABASE_FAIL
	}
	
	private NewUserErrorType newUserErrorType;
	
	/**
	 * Method that gets the type of error for the creation of a new user
	 * @return
	 */
	public NewUserErrorType getNewUserErrorType() {
		return newUserErrorType;
	}
	
	/**
	 * Class constructor
	 * @param newUserErrorType
	 */
	public NewUserException(NewUserErrorType newUserErrorType) {
		this.newUserErrorType = newUserErrorType;
	}

}
