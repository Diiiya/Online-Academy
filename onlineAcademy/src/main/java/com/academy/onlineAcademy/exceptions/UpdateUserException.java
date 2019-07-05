package com.academy.onlineAcademy.exceptions;

public class UpdateUserException extends Exception {

	public enum UpdateUserExErrorType {
		EXISTING_EMAIL, VALIDATION_FAILED, DATABASE_FAILED
	}
	
	private UpdateUserExErrorType updateUserExErrortype;
	
	public UpdateUserExErrorType getUpdateUserExErrorType() {
		return updateUserExErrortype;
	}
	
	public UpdateUserException(UpdateUserExErrorType updateUserExErrorType) {
		this.updateUserExErrortype = updateUserExErrorType;
	}
	
}