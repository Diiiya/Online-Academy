package com.academy.onlineAcademy.exceptions;

public class NewCourseException extends Exception {
	
	public enum NewCourseTypeError {
		VALIDATION_FAILED, EXISTING_COURSE, DURATION_NUMERIC_VALUE, PRICE_NUMERIC_VALUE, CATEGORY_REQUIRED, DATABASE_FAIL
	}
	
	private NewCourseTypeError newCourseTypeError;
	
	public NewCourseTypeError getNewCourseTypeError () {
		return newCourseTypeError;
	}
	
	public NewCourseException(NewCourseTypeError newCourseTypeError) {
		this.newCourseTypeError = newCourseTypeError;
	}

}
