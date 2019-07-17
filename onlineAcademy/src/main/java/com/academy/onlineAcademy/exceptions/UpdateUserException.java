package com.academy.onlineAcademy.exceptions;

public class UpdateUserException extends Exception {

	/**
	 * Enum of error types thrown when updating a user
	 * @author d.boyadzhieva
	 *
	 */
	public enum UpdateUserExErrorType {
		EXISTING_EMAIL, PASSWORDS_NOT_MATCHING, VALIDATION_FAILED, DATABASE_FAILED
	}
	
	private UpdateUserExErrorType updateUserExErrortype;
	
	/**
	 * Method that gets the type of error for updating user
	 * @return
	 */
	public UpdateUserExErrorType getUpdateUserExErrorType() {
		return updateUserExErrortype;
	}
	
	/**
	 * Class constructor
	 * @param updateUserExErrorType
	 */
	public UpdateUserException(UpdateUserExErrorType updateUserExErrorType) {
		this.updateUserExErrortype = updateUserExErrorType;
	}
	
}
